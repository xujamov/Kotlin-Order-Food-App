package com.muratozturk.orderfood.ui.basket

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratozturk.orderfood.data.models.ProductsBasketRoomModel
import com.muratozturk.orderfood.data.repo.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class BasketViewModel(context: Context) : ViewModel() {

    private val repository = Repository(context)
    var basketList: LiveData<List<ProductsBasketRoomModel>> = repository.basketList
    var basketTotalAmount: LiveData<Double> = repository.basketTotalAmount
    var isLoading: LiveData<Repository.LOADING> = repository.isLoading

    fun getBasket() {
        viewModelScope.launch {
            // Bottom Navigation Bar is lagging if don't add delay
            delay(500)
            repository.getBasket()
            getBasketTotalAmount()
        }

    }

    fun deleteProductFromBasket(productId: UUID) {
        viewModelScope.launch {
            repository.deleteBookFromBasket(productId)
            getBasketTotalAmount()
        }

    }

    private fun getBasketTotalAmount() {
        viewModelScope.launch {
            repository.getBasketTotalAmount()
        }
    }
}