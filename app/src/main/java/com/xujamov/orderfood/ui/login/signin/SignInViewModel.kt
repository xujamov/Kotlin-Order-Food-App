package com.xujamov.orderfood.ui.login.signin

import android.app.Activity
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xujamov.orderfood.data.repo.UserRepository

class SignInViewModel(activity: Activity) : ViewModel() {

    private var usersRepo = UserRepository(activity)
    val isSignIn: LiveData<Boolean> = usersRepo.isSignIn
    val isLoading: LiveData<UserRepository.LOADING> = usersRepo.isLoading
    val allowVerification: LiveData<Boolean> = usersRepo.allowVerification

        private var _isInfosValid = MutableLiveData<Boolean>()
    val isInfosValid: LiveData<Boolean> = _isInfosValid
    fun signIn(phone: String, verifyCode: String) {
        if (allowVerification.value == true) {
            usersRepo.signInWithCode(verifyCode, phone)
        } else if (phone.isNotEmpty() && Patterns.PHONE.matcher(phone).matches()) {
            _isInfosValid.value = true
            usersRepo.signIn(phone)
        } else {
            _isInfosValid.value = false
        }
    }
}