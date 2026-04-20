package com.example.moneymate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneymate.data.local.dao.BudgetDao
import com.example.moneymate.data.local.dao.CategoryDao
import com.example.moneymate.data.local.dao.SavingGoalDao
import com.example.moneymate.data.local.dao.TransactionDao
import com.example.moneymate.data.local.dao.UserDAO
import com.example.moneymate.data.local.entity.BudgetEntity
import com.example.moneymate.data.local.entity.CategoryEntity
import com.example.moneymate.data.local.entity.NotificationEntity
import com.example.moneymate.data.local.entity.SavingGoalEntity
import com.example.moneymate.data.local.entity.SavingTransactionEntity
import com.example.moneymate.data.local.entity.TransactionEntity
import com.example.moneymate.data.local.entity.User

@Database(entities = [
                    User::class,
                    TransactionEntity::class,
                    CategoryEntity::class,
                    BudgetEntity::class,
                    SavingGoalEntity::class,
                    SavingTransactionEntity::class,
                    NotificationEntity::class
          ],
    version = 2)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun TransactionDao(): TransactionDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun BudgetDao(): BudgetDao
    abstract fun SavingGoalDao(): SavingGoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}