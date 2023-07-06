package com.muratozturk.orderfood.data.repo

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.muratozturk.orderfood.data.models.UserModel
import java.util.concurrent.TimeUnit

class UserRepository (private val activity: Activity){

    enum class LOADING {
        LOADING, DONE, ERROR
    }

    var isSignIn = MutableLiveData<Boolean>()
    var userInfo = MutableLiveData<UserModel>()
    var isLoading = MutableLiveData<LOADING>()

    private var auth = Firebase.auth
    private val db = Firebase.firestore
    val firebaseAuthSettings = auth.firebaseAuthSettings

    val phoneNum = "+998338819995"
    val testVerificationCode = "654321"

    fun signIn(phone: String) {
        isLoading.value = LOADING.LOADING

        // Configure faking the auto-retrieval with the whitelisted numbers.
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNum, testVerificationCode)

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNum) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval or instant verification has succeeded
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Verification has failed
                    // Handle the error

                    isLoading.value = LOADING.ERROR
                    isSignIn.value = false
                    Log.w("SIGN_IN", "FAILURE", e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.

                    // Save verification ID and resending token so we can use them later

                }

                // ... additional callbacks (optional)
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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
                            "phonenumber" to phoneNum
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
                    isLoading.value = LOADING.DONE
                    isSignIn.value = true
                    Log.d("SIGN_IN", "SUCCESS")
                } else {
                    // Sign in failed
                    // Handle the error
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