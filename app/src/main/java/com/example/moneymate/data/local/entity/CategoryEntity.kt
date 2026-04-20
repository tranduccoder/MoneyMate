package com.example.moneymate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val user_id: Int,
    val name: String,
    val type: String, // "income" hoặc "expense"
    val icon: Int  // lưu tên icon (vd: "ic_food")
)