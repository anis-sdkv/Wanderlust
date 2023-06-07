package com.wanderlust.domain.services

import com.wanderlust.domain.action_results.LoginResult
import com.wanderlust.domain.action_results.RegisterResult

interface UserService {
    suspend fun register(username: String, email: String, password: String): RegisterResult
    suspend fun sendVerification(): Boolean
    suspend fun login(email: String, password: String): LoginResult
    suspend fun updatePassword(newPassword: String): Boolean
    fun signOut()
}