package com.wanderlust.domain.repositories

import com.wanderlust.domain.model.UserProfile

interface UserRepository {
    suspend fun createUser(id: String, username: String)
    suspend fun getById(id: String): UserProfile?
    suspend fun update(userProfile: UserProfile)
}