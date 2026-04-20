package com.example.moneymate.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.moneymate.data.local.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE user_id = :userId")
    fun getAll(userId: Int): LiveData<List<CategoryEntity>>

    @Insert
    suspend fun insert(category: CategoryEntity)
    @Update
    suspend fun update(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)
    @Query("SELECT * FROM categories WHERE user_id = :userId AND type = :type")
    fun getAll(userId: Int,type: String): LiveData<List<CategoryEntity>>
}