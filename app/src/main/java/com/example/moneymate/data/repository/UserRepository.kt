package com.example.moneymate.data.repository

import com.example.moneymate.data.local.dao.UserDAO
import com.example.moneymate.data.local.entity.User

class UserRepository (private val userDao: UserDAO) {
    suspend fun register(user: User)  {
        userDao.insert(user)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }
    suspend fun checkEmail(email: String): User? {
        return userDao.findByEmail(email)
    }


}
