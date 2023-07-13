package com.xujamov.orderfood.common.utils
import android.content.Context
import android.content.SharedPreferences

class TokenManager(private val context: Context) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val tokenKey = "token"

    fun saveToken(token: String) {
        sharedPrefs.edit().putString(tokenKey, token).apply()
    }

    fun getToken(): String? {
        return sharedPrefs.getString(tokenKey, null)
    }

    fun clearToken() {
        sharedPrefs.edit().remove(tokenKey).apply()
    }
}