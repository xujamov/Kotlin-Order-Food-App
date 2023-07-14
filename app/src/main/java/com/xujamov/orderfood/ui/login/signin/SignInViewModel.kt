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
    val verifyId: LiveData<String> = usersRepo.verifyId

    private var _isPhoneValid = MutableLiveData<Boolean>()
    val isPhoneValid: LiveData<Boolean> = _isPhoneValid

    fun signIn(phone: String) {
        if (phone.isNotEmpty() && Patterns.PHONE.matcher(phone).matches()) {
            _isPhoneValid.value = true
            usersRepo.signIn(phone)
        } else {
            _isPhoneValid.value = false
        }
    }
}