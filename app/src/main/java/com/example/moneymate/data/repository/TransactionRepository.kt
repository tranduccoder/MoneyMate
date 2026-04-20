package com.example.moneymate.data.repository

import com.example.moneymate.data.local.dao.TransactionDao
import com.example.moneymate.data.local.entity.TransactionEntity

class TransactionRepository(private val dao: TransactionDao) {

    val transactions = dao.getAllTransactionFull()
    val income = dao.getIncome()
    val expense = dao.getExpense()
    suspend fun insert(transaction: TransactionEntity) {
        dao.insert(transaction)
    }

    suspend fun delete(transaction: TransactionEntity) {
        dao.delete(transaction)
    }

    suspend fun update(transaction: TransactionEntity) {
        dao.update(transaction)
    }
}