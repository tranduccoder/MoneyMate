package com.example.moneymate.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moneymate.data.local.entity.BudgetEntity

@Dao
interface BudgetDao {

    @Insert
    suspend fun insert(budget: BudgetEntity)

    @Query("SELECT * FROM budgets WHERE user_id = :userId")
    fun getBudgets(userId: Int): LiveData<List<BudgetEntity>>
}