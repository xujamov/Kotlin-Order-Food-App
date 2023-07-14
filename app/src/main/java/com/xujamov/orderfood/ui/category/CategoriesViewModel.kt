package com.xujamov.orderfood.ui.category

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xujamov.orderfood.common.utils.TokenManager
import com.xujamov.orderfood.data.models.Categories
import com.xujamov.orderfood.data.repo.Repository
import kotlinx.coroutines.launch

class CategoriesViewModel(context: Context, tokenManager: TokenManager?) : ViewModel() {

    private val repository = Repository(context, tokenManager)

    var categoryList: LiveData<List<Categories>> = repository.categoriesList
    var isLoading: LiveData<Repository.LOADING> = repository.isLoading


    fun getCategories() {

        viewModelScope.launch {
            repository.getCategories()
        }
    }

}