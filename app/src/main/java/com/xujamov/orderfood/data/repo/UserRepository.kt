package com.xujamov.orderfood.data.repo

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xujamov.orderfood.data.models.UserModel
import java.util.concurrent.TimeUnit

class UserRepository (private val activity: Activity){

    enum class LOADING {
        LOADING, DONE, ERROR
    }

    var isSignIn = MutableLiveData<Boolean>()
    var userInfo = MutableLiveData<UserModel>()
    var isLoading = MutableLiveData<LOADING>()
    var verifyId = MutableLiveData<String>()

    private var auth = Firebase.auth
    private val db = Firebase.firestore

//    val firebaseAuthSettings = auth.firebaseAuthSettings
//
//    val phoneNum = "+998338819995"
//    val testVerificationCode = "654321"

    fun signIn(phone: String) {
        isLoading.value = LOADING.LOADING

        val TAG = "SIGN_IN"

        // Configure faking the auto-retrieval with the whitelisted numbers.
//        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNum, testVerificationCode)
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                isSignIn.value = false
                isLoading.value = LOADING.ERROR

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
//                else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                    // reCAPTCHA verification attempted with null Activity
//                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                isLoading.value = LOADING.DONE
                verifyId.value = verificationId

            }
        }


        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithCode(verifyId: String, code: String) {
        isLoading.value = LOADING.LOADING
        val credential = PhoneAuthProvider.getCredential(verifyId, code)
        // Use the credential to sign in the user
         auth.signInWithCredential(credential)
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     // User successfully signed in
                     val currentUser = auth.currentUser
                     currentUser?.let { fbUser ->
                         val user = hashMapOf(
                             "id" to fbUser.uid,
                             "email" to "",
                             "phonenumber" to fbUser.phoneNumber
                         )

                         db.collection("users").document(fbUser.uid)
                             .set(user)
                             .addOnSuccessListener {
                                 isSignIn.value = true
                                 isLoading.value = LOADING.DONE
                                 Log.d("SIGN_IN", "SUCCESS")
                             }
                             .addOnFailureListener { e ->
                                 isSignIn.value = false
                                 isLoading.value = LOADING.ERROR
                                 Log.w("SIGN_IN", "FAILURE", e)
                             }
                     }
                 } else {
                     // Sign in failed
                     // Handle the error
                     isSignIn.value = false
                     isLoading.value = LOADING.ERROR
                 }
             }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        isLoading.value = LOADING.LOADING
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User successfully signed in
                    val currentUser = auth.currentUser
                    currentUser?.let { fbUser ->
                        val user = hashMapOf(
                            "id" to fbUser.uid,
                            "email" to "",
                            "phonenumber" to fbUser.phoneNumber
                        )

                        db.collection("users").document(fbUser.uid)
                            .set(user)
                            .addOnSuccessListener {
                                isSignIn.value = true
                                isLoading.value = LOADING.DONE
                                Log.d("SIGN_IN", "SUCCESS")
                            }
                            .addOnFailureListener { e ->
                                isSignIn.value = false
                                isLoading.value = LOADING.ERROR
                                Log.w("SIGN_IN", "FAILURE", e)
                            }
                    }
                } else {
                    // Sign in failed
                    // Handle the error
                    isSignIn.value = false
                    isLoading.value = LOADING.ERROR
                }
            }
    }

    fun getUserInfo() {
        isLoading.value = LOADING.LOADING
        auth.currentUser?.let { user ->

            val docRef = db.collection("users").document(user.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        userInfo.value = UserModel(
                            user.email,
                            document.get("phonenumber") as String
                        )
                        isLoading.value = LOADING.DONE
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                    isLoading.value = LOADING.ERROR
                }
        }
    }

    fun signOut() {
        isLoading.value = LOADING.LOADING
        auth.signOut()
        isLoading.value = LOADING.DONE
    }
}