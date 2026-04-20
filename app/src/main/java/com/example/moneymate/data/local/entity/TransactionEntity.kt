package com.example.moneymate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val user_id: Int,
    val category_id: Int,

    val amount: Double,
    val type: String,      // "income" | "expense"
    val note: String,

    val date: String,      // ví dụ: "2026-03-29"
    val created_at: String // timestamp
)