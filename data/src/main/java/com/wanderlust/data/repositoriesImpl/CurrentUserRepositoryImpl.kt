package com.wanderlust.data.repositoriesImpl

import com.wanderlust.data.services.UserService
import com.wanderlust.data.sources.local.sharedpref.AppPreferences
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.CurrentUserRepository
import com.wanderlust.domain.repositories.UserRepository
import javax.inject.Inject

class CurrentUserRepositoryImpl @Inject constructor(
    private val preferences: AppPreferences,
    private val userRepository: UserRepository,
    private val userService: UserService
) : CurrentUserRepository {
    private var savedUser: UserProfile? = null

    override suspend fun get(): UserProfile? {
        return if (savedUser != null) return savedUser
        else {
            val id = preferences.getUserId()
            id?.let { userRepository.getById(it) }
        }
    }

    override fun set(id: String) {
        preferences.saveUserId(id)
    }

    override fun signOut() {
        userService.signOut()
        savedUser = null
        preferences.saveUserId(null)
    }

    override fun notifyUpdated() {
        savedUser = null
    }
}