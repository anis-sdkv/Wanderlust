package com.wanderlust.domain.repositories

import com.wanderlust.domain.model.User

interface UserRepository {
    suspend fun createUser(id: String, username: String): User
    fun getUserByUserName(name: String): User
}