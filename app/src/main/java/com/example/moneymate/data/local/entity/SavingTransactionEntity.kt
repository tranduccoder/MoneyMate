package com.example.moneymate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving_transactions")
data class SavingTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val goal_id: Int,
    val amount: Double,
    val date: String,
    val note: String
)