package com.example.moneymate.data.repository

import com.example.moneymate.data.local.dao.CategoryDao
import com.example.moneymate.data.local.entity.CategoryEntity

class CategoryRepository(private val dao: CategoryDao) {

    suspend fun insert(category: CategoryEntity) {
        dao.insert(category)
    }

    suspend fun update(category: CategoryEntity) {
        dao.update(category)
    }

    suspend fun delete(category: CategoryEntity) {
        dao.delete(category)
    }

    fun getAll(userId: Int) = dao.getAll(userId)
    fun getByType(userId: Int, type: String) = dao.getAll(userId, type)

}