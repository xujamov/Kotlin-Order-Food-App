package com.xujamov.orderfood.ui.login.verify

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xujamov.orderfood.data.repo.UserRepository

class VerifyModel(activity: Activity) : ViewModel() {

    private var usersRepo = UserRepository(activity)
    val isSignIn: LiveData<Boolean> = usersRepo.isSignIn
    val isLoading: LiveData<UserRepository.LOADING> = usersRepo.isLoading

    private var _isCodeValid = MutableLiveData<Boolean>()
    val isCodeValid: LiveData<Boolean> = _isCodeValid
    fun signInWithCode(verifyId: String, verifyCode: String) {
        if (verifyCode.isNotEmpty()) {
            _isCodeValid.value = true
            usersRepo.signInWithCode(verifyId, verifyCode)
        } else {
            _isCodeValid.value = false
        }
    }
}