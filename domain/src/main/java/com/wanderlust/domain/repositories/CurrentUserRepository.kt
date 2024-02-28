package com.wanderlust.domain.repositories

import com.wanderlust.domain.model.UserProfile

interface CurrentUserRepository {
    suspend fun get(): UserProfile?
    fun getId(): String?
    fun set(id: String)
    fun notifyUpdated()
    fun signOut()
}