package com.example.moneymate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymate.data.local.entity.TransactionEntity
import com.example.moneymate.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val transactions = repository.transactions
    val income = repository.income
    val expense = repository.expense

    fun insert(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.insert(transaction)
        }
    }

    fun update(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.update(transaction)
        }
    }

    fun delete(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }
}