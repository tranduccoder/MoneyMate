package com.example.moneymate.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moneymate.data.local.entity.SavingTransactionEntity

@Dao
interface SavingTransactionDao {

    @Insert
    suspend fun insert(transaction: SavingTransactionEntity)

    @Query("SELECT * FROM saving_transactions WHERE goal_id = :goalId")
    fun getByGoal(goalId: Int): LiveData<List<SavingTransactionEntity>>
}