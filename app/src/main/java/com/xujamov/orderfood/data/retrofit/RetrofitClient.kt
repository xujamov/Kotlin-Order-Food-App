package com.xujamov.orderfood.data.retrofit

import android.content.Intent
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xujamov.orderfood.ui.MainActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private var auth = Firebase.auth
        private var token = ""
        fun getClient(baseUrl: String): Retrofit {

            auth.currentUser?.let {
                it.getIdToken(true).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        token = task.result?.token.toString()
                    } else {
                        // Handle error in getting the token
                    }
                }
            }
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