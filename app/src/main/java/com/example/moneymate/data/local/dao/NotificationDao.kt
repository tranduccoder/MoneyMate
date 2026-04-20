package com.example.moneymate.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moneymate.data.local.entity.NotificationEntity

@Dao
interface NotificationDao {

    @Insert
    suspend fun insert(notification: NotificationEntity)

    @Query("SELECT * FROM notifications WHERE user_id = :userId")
    fun getNotifications(userId: Int): LiveData<List<NotificationEntity>>
}