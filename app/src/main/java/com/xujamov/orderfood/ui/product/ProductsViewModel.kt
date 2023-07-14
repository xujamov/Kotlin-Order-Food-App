package com.xujamov.orderfood.ui.product


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xujamov.orderfood.common.utils.TokenManager
import com.xujamov.orderfood.data.models.Product
import com.xujamov.orderfood.data.repo.Repository
import kotlinx.coroutines.launch
import java.util.UUID

class ProductsViewModel(context: Context, tokenManager: TokenManager?) : ViewModel() {

    private val repository = Repository(context, tokenManager)
    var productList: LiveData<List<Product>> = repository.productList
    var isLoading: LiveData<Repository.LOADING> = repository.isLoading


    fun getProducts(categoryId: UUID) {
        viewModelScope.launch {
            repository.getProducts(categoryId)
        }


    }

}