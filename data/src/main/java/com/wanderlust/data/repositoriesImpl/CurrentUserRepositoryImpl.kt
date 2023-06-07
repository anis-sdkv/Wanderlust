package com.wanderlust.data.repositoriesImpl

import com.wanderlust.data.services.UserServiceImpl
import com.wanderlust.data.sources.local.sharedpref.AppPreferences
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.CurrentUserRepository
import com.wanderlust.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrentUserRepositoryImpl @Inject constructor(
    private val preferences: AppPreferences,
    private val userRepository: UserRepository,
    private val userService: UserServiceImpl
) : CurrentUserRepository {
    private var savedUser: UserProfile? = null

    override suspend fun get(): UserProfile? = withContext(Dispatchers.IO) {
        if (savedUser != null) savedUser
        else preferences.getUserId()?.let { userRepository.getById(it) }
    }

    override fun getId(): String? =
        if (savedUser != null) savedUser!!.id else preferences.getUserId()

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