package com.xujamov.orderfood.data.retrofit


import com.xujamov.orderfood.data.models.Categories
import com.xujamov.orderfood.data.models.Order
import com.xujamov.orderfood.data.models.OrderStatus
import com.xujamov.orderfood.data.models.Product
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

    @POST("billdetails")
    suspend fun addOrder(@Body order: Order): Response<Order>

    @POST("billstatus")
    suspend fun addOrderStatus(@Body order: OrderStatus): Response<OrderStatus>
}