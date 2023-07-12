package com.xujamov.orderfood.ui.payment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xujamov.orderfood.data.repo.Repository
import kotlinx.coroutines.launch

class PaymentViewModel(context: Context) : ViewModel() {

    private val repository = Repository(context)
    
    fun clearBasket() {
        viewModelScope.launch {
            repository.clearBasket()
        }
    }

    fun createOrder() {
        viewModelScope.launch {
            repository.addOrder()
        }
    }
}