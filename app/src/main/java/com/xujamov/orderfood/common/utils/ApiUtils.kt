package com.xujamov.orderfood.common.utils

import com.xujamov.orderfood.data.retrofit.DAOInterface
import com.xujamov.orderfood.data.retrofit.RetrofitClient

class ApiUtils {

    companion object {
        private const val BASE_URL = "https://restaurant-ordering-system.onrender.com/api/"


        fun getInterfaceDAO(): DAOInterface {
            return RetrofitClient.getClient(BASE_URL).create(DAOInterface::class.java)
        }
    }
}