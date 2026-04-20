package com.example.moneymate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneymate.data.repository.TransactionRepository

class TransactionViewModelFactory(
    private val repo: TransactionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionViewModel(repo) as T
    }
}