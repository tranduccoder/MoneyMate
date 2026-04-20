package com.example.moneymate.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneymate.data.repository.CategoryRepository
import com.example.moneymate.viewmodel.CategoryViewModel

class CategoryViewModelFactory(
    private val repository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(repository) as T
    }
}