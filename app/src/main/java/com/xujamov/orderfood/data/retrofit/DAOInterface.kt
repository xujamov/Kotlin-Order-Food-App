package com.xujamov.orderfood.data.retrofit


import com.xujamov.orderfood.data.models.Categories
import com.xujamov.orderfood.data.models.Order
import com.xujamov.orderfood.data.models.Product
import com.xujamov.orderfood.data.models.ProductsBasketRoomModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

interface DAOInterface {

    @GET("getCategories")
    suspend fun getCategories(): Response<List<Categories>>

    @GET("getCategoryProducts")
    suspend fun getProducts(@Query("categoryId") categoryId: UUID): Response<List<Product>>

    @GET("getProducts")
    suspend fun getSearch(): Response<List<Product>>

    @POST("billstatus")
    suspend fun addOrder(@Body order: List<ProductsBasketRoomModel>?): Response<String>
}