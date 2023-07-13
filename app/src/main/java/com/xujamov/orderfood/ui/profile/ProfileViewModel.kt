package com.xujamov.orderfood.ui.profile

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.xujamov.orderfood.common.utils.TokenManager
import com.xujamov.orderfood.data.models.UserModel
import com.xujamov.orderfood.data.repo.UserRepository

class ProfileViewModel(activity: Activity, tokenManager: TokenManager?) : ViewModel() {

    private val usersRepo = UserRepository(activity, tokenManager)

    val userInfo: LiveData<UserModel>
        get() = usersRepo.userInfo

    val isLoading: LiveData<UserRepository.LOADING> = usersRepo.isLoading

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        usersRepo.getUserInfo()
    }

    fun signOut() {
        usersRepo.signOut()
    }
}