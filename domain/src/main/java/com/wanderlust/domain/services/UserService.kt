package com.wanderlust.domain.services

import com.wanderlust.domain.model.RegisterResult

interface UserService {
    suspend fun register(username: String, email: String, password: String): RegisterResult

    suspend fun sendVerification(): Boolean

    suspend fun login(email: String, password: String): Boolean

    suspend fun updatePassword(newPassword: String): Boolean
}