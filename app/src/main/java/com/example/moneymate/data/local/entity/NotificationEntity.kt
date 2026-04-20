package com.example.moneymate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user_id: Int,
    val title: String,
    val message: String,
    val type: String, // warning / reminder
    val is_read: Int, // 0 / 1
    val created_at: String
)