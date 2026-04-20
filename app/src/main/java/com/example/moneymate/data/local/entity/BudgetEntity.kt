package com.example.moneymate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user_id: Int,
    val category_id: Int,
    val amount: Double,
    val start_date: String,
    val end_date: String
)