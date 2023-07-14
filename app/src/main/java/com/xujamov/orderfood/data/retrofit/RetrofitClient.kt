package com.xujamov.orderfood.data.retrofit

import android.content.Intent
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xujamov.orderfood.common.utils.TokenManager
import com.xujamov.orderfood.ui.MainActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        fun getClient(baseUrl: String, tokenManager: TokenManager?): Retrofit {

            val token = tokenManager?.getToken()?.takeIf { it.isNotEmpty() } ?: ""

            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(newRequest)
                })
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}