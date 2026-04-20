package com.example.moneymate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymate.data.local.entity.CategoryEntity
import com.example.moneymate.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    val categories = repository.getAll(1)

    fun insert(category: CategoryEntity) {
        viewModelScope.launch {
            repository.insert(category)
        }
    }

    fun update(category: CategoryEntity) {
        viewModelScope.launch {
            repository.update(category)
        }
    }

    fun delete(category: CategoryEntity) {
        viewModelScope.launch {
            repository.delete(category)
        }
    }
    fun getByType(type: String) = repository.getByType(1, type)
}