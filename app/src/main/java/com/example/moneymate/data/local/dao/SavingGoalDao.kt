package com.example.moneymate.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moneymate.data.local.entity.SavingGoalEntity

@Dao
interface SavingGoalDao {

    @Insert
    suspend fun insert(goal: SavingGoalEntity)

    @Query("SELECT * FROM saving_goals WHERE user_id = :userId")
    fun getGoals(userId: Int): LiveData<List<SavingGoalEntity>>
}