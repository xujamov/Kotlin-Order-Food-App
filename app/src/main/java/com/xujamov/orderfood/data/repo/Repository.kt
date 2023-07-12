package com.xujamov.orderfood.data.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.xujamov.orderfood.data.models.Categories
import com.xujamov.orderfood.data.models.Product
import com.xujamov.orderfood.common.utils.ApiUtils
import com.xujamov.orderfood.data.models.Order
import com.xujamov.orderfood.data.models.ProductsBasketRoomModel
import com.xujamov.orderfood.data.retrofit.DAOInterface
import com.xujamov.orderfood.data.room.ProductsBasketDAOInterface
import com.xujamov.orderfood.data.room.ProductsBasketRoomDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.UUID

class Repository(context: Context) {
    enum class LOADING {
        LOADING, DONE, ERROR
    }

    val categoriesList = MutableLiveData<List<Categories>>()
    val productList = MutableLiveData<List<Product>>()
    val searchList = MutableLiveData<List<Product>>()
    val basketList = MutableLiveData<List<ProductsBasketRoomModel>>()
    val isLoading = MutableLiveData<LOADING>()
    val basketTotalAmount = MutableLiveData<Double>()

    private val dif: DAOInterface = ApiUtils.getInterfaceDAO()
    private val basketDif: ProductsBasketDAOInterface? =
        ProductsBasketRoomDatabase.productsBasketRoomDatabase(context)?.productsBasketDAOInterface()

    suspend fun getCategories() {
        try {
            isLoading.value = LOADING.LOADING
            // Bottom Navigation Bar is lagging if don't add delay
            delay(500)
            val response = dif.getCategories()
            if (response.isSuccessful) {
                val tempList = response.body()
                categoriesList.value = tempList!!
                isLoading.value = LOADING.DONE
            } else {
                isLoading.value = LOADING.ERROR
            }
        } catch (t: Throwable) {
            t.localizedMessage?.toString()?.let { Log.e("getCategories", it) }
            isLoading.value = LOADING.ERROR
        }

    }

    suspend fun getProducts(categoryId: UUID) {
        try {
            isLoading.value = LOADING.LOADING
            // Bottom Navigation Bar is lagging if don't add delay
            delay(500)
            val response = dif.getProducts(categoryId)
            if (response.isSuccessful) {
                val tempList = response.body()
                productList.value = tempList!!
                isLoading.value = LOADING.DONE
            } else {
                isLoading.value = LOADING.ERROR
            }
        } catch (t: Throwable) {
            t.localizedMessage?.toString()?.let { Log.e("getCategories", it) }
            isLoading.value = LOADING.ERROR
        }

    }

    suspend fun getSearch() {
        try {

            isLoading.value = LOADING.LOADING
            // Bottom Navigation Bar is lagging if don't add delay
            delay(500)
            val response = dif.getSearch()
            if (response.isSuccessful) {
                val tempList = response.body()
                searchList.value = tempList!!
                isLoading.value = LOADING.DONE
            } else {
                isLoading.value = LOADING.ERROR

            }
        } catch (t: Throwable) {
            t.localizedMessage?.toString()?.let { Log.e("getCategories", it) }
            isLoading.value = LOADING.ERROR
        }

    }

    fun getBasket() = runBlocking {
        isLoading.value = LOADING.LOADING

        basketDif?.getProductsBasket()?.let {
            basketList.value = it
            isLoading.value = LOADING.DONE
        } ?: run {
            isLoading.value = LOADING.DONE
        }
    }

    fun getBasketTotalAmount() = runBlocking {

        if (basketDif?.getProductsBasketTotalAmount() != null) {
            basketTotalAmount.value = basketDif.getProductsBasketTotalAmount()
        } else {
            basketTotalAmount.value = 0.00
        }


    }

    suspend fun addBookToBasket(productModel: ProductsBasketRoomModel) {

        val productFromDatabase = basketDif?.getProductById(productModel.productId)

        if (productFromDatabase != null) {
            val productCount = productFromDatabase.productCount + productModel.productCount
            val productPrice =
                (productFromDatabase.productPrice?.div(productFromDatabase.productCount))?.times(
                    productCount
                )

            basketDif?.updateProductBasket(productModel.productId, productCount, productPrice)

        } else {
            basketDif?.addProductBasket(productModel)
        }


    }

    suspend fun addOrder() {
        try {
            isLoading.value = LOADING.LOADING
            // Bottom Navigation Bar is lagging if don't add delay
            delay(500)

            val basket = basketDif?.getProductsBasket()

            Log.d("Doniyor", basket.toString())

            val response = dif.addOrder(basket)
            if (response.isSuccessful) {
                isLoading.value = LOADING.DONE
            } else {
                isLoading.value = LOADING.ERROR
            }
        } catch (t: Throwable) {
            t.localizedMessage?.toString()?.let { Log.e("getCategories", it) }
            isLoading.value = LOADING.ERROR
        }

    }

    suspend fun deleteBookFromBasket(productId: UUID) {
        basketDif?.deleteProductWithId(productId)
    }

    suspend fun clearBasket() {
        basketDif?.clearBasket()
    }
}