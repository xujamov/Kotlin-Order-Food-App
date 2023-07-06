package com.xujamov.orderfood.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.xujamov.orderfood.data.models.ProductsBasketRoomModel
import java.util.UUID

@Dao
interface ProductsBasketDAOInterface {

    @Insert
    suspend fun addProductBasket(productsBasketRoomModel: ProductsBasketRoomModel)

    @Query("SELECT * FROM productsbasketdatabase")
    suspend fun getProductsBasket(): List<ProductsBasketRoomModel>?

    @Query("SELECT Sum(ProductPrice) FROM productsbasketdatabase")
    suspend fun getProductsBasketTotalAmount(): Double?

    @Query("SELECT * FROM productsbasketdatabase WHERE id = :productId  LIMIT 1")
    suspend fun getProductById(productId: UUID): ProductsBasketRoomModel?

    @Query("DELETE FROM productsbasketdatabase WHERE id = :idInput")
    suspend fun deleteProductWithId(idInput: UUID)

    @Query("DELETE FROM productsbasketdatabase")
    suspend fun clearBasket()

    @Query("UPDATE productsbasketdatabase SET productCount=:productCount, productPrice=:productPrice  WHERE id = :productId")
    suspend fun updateProductBasket(productId: UUID, productCount: Int, productPrice: Double?)
}