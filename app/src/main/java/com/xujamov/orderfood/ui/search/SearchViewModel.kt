package com.xujamov.orderfood.ui.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xujamov.orderfood.data.models.Product
import com.xujamov.orderfood.data.repo.Repository
import kotlinx.coroutines.launch

class SearchViewModel(context: Context) : ViewModel() {

    private val repository = Repository(context)
    var productList: LiveData<List<Product>> = repository.searchList
    var isLoading: LiveData<Repository.LOADING> = repository.isLoading


    fun getSearch() {
        viewModelScope.launch {
            repository.getSearch()
        }
    }
}