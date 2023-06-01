package com.wanderlust.domain.repositories

import com.wanderlust.domain.model.UserProfile

interface CurrentUserRepository {
    suspend fun get(): UserProfile?
    fun set(id: String)
    fun notifyUpdated()
}