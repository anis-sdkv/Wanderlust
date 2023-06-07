package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.Route
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.UserRepository

class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: String) : UserProfile? {
        return userRepository.getById(id)
    }
}