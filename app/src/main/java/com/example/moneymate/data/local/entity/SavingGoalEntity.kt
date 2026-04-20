package com.example.moneymate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving_goals")
data class SavingGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user_id: Int,
    val title: String,
    val target_amount: Double,
    val current_amount: Double,
    val start_date: String,
    val end_date: String,
    val status: String // ongoing / completed
)