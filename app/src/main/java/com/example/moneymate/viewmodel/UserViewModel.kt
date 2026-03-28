package com.example.moneymate.viewmodel

import android.app.Application
import androidx.activity.result.ActivityResultCallback
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymate.data.local.AppDatabase
import com.example.moneymate.data.local.entity.User
import com.example.moneymate.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository
    init {
        val dao = AppDatabase.getInstance(application).userDao()
        userRepository = UserRepository(dao)

    }
    fun login(email: String, password: String, callback: (User?)-> Unit){
        viewModelScope.launch {
            val user = userRepository.login(email, password)
            callback(user)
        }
    }
    fun register(user: User) {
        viewModelScope.launch {
            userRepository.register(user)
        }
    }
    fun checkEmail(email: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.checkEmail(email)
            callback(user)
        }
    }
}
