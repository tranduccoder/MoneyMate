package com.example.moneymate.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.moneymate.data.local.entity.CategoryStats
import com.example.moneymate.data.local.entity.TransactionEntity
import com.example.moneymate.data.local.entity.TransactionFull

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: TransactionEntity)
    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income'")
    fun getIncome(): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense'")
    fun getExpense(): LiveData<Double?>
    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 5")
    fun getRecentTransactions(): LiveData<List<TransactionEntity>>
    // 🔥 FULL LIST + CATEGORY
    @Query("""
    SELECT t.*, c.name AS categoryName, c.icon AS categoryIcon
    FROM transactions t
    INNER JOIN categories c
    ON t.category_id = c.id
    ORDER BY t.date DESC
""")
    fun getAllTransactionFull(): LiveData<List<TransactionFull>>

    // 🔥 5 GIAO DỊCH GẦN NHẤT
    @Query("""
    SELECT t.*, c.name AS categoryName, c.icon AS categoryIcon
    FROM transactions t
    INNER JOIN categories c
    ON t.category_id = c.id
    ORDER BY t.date DESC
    LIMIT 5
""")

    fun getRecentTransactionFull(): LiveData<List<TransactionFull>>

    // 🔥 THỐNG KÊ THEO DANH MỤC
    @Query("""
SELECT c.name AS categoryName, SUM(t.amount) AS total
FROM transactions t
JOIN categories c ON t.category_id = c.id
WHERE t.type = :type
GROUP BY t.category_id
""")
    fun getStatsByType(type: String): LiveData<List<CategoryStats>>

    @Query("""
SELECT c.name AS categoryName, SUM(t.amount) AS total
FROM transactions t
JOIN categories c ON t.category_id = c.id
WHERE t.type = :type
AND t.date BETWEEN :startDate AND :endDate
GROUP BY t.category_id
""")
    fun getStatsByTypeAndDate(
        type: String,
        startDate: String,
        endDate: String
    ): LiveData<List<CategoryStats>>

    @Query("""
SELECT t.*, c.name AS categoryName, c.icon AS categoryIcon
FROM transactions t
INNER JOIN categories c ON t.category_id = c.id
WHERE t.date BETWEEN :startDate AND :endDate
ORDER BY t.date DESC
""")
    fun getTransactionByDateRange(
        startDate: String,
        endDate: String
    ): LiveData<List<TransactionFull>>
    @Query("""
SELECT SUM(amount) FROM transactions
WHERE type = 'income'
AND date BETWEEN :startDate AND :endDate
""")
    fun getIncomeByDate(startDate: String, endDate: String): LiveData<Double?>

    @Query("""
SELECT SUM(amount) FROM transactions
WHERE type = 'expense'
AND date BETWEEN :startDate AND :endDate
""")
    fun getExpenseByDate(startDate: String, endDate: String): LiveData<Double?>

}

