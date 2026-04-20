package com.example.moneymate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymate.data.local.entity.TransactionEntity
import com.example.moneymate.data.local.entity.TransactionFull
import com.example.moneymate.data.repository.TransactionRepository

class HomeViewModel(repository: TransactionRepository) : ViewModel() {

    val transactions: LiveData<List<TransactionFull>> = repository.transactions
    val income: LiveData<Double?> = repository.income
    val expense: LiveData<Double?> = repository.expense

    val balance = MediatorLiveData<Double>()

    init {
        balance.addSource(income) { updateBalance() }
        balance.addSource(expense) { updateBalance() }
    }

    private fun updateBalance() {
        val i = income.value ?: 0.0
        val e = expense.value ?: 0.0
        balance.value = i - e
    }
}