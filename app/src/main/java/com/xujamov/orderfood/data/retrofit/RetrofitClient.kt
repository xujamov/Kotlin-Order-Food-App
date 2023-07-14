package com.xujamov.orderfood.data.retrofit

import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        fun getClient(baseUrl: String): Retrofit {

            val currentUser = FirebaseAuth.getInstance().currentUser
            val idToken = currentUser?.getIdToken(/* forceRefresh */ false)?.result?.token

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $idToken")
                        .build()
                    chain.proceed(newRequest)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}