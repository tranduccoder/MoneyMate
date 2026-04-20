package com.example.moneymate.data.local.entity

data class TransactionFull(
    val id: Int,
    val user_id: Int,
    val category_id: Int,
    val amount: Double,
    val type: String,
    val note: String,
    val date: String,
    val created_at: String,

    val categoryName: String,
    val categoryIcon: Int
)