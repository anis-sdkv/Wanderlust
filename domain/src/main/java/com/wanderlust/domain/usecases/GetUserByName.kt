package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.UserRepository

class GetUserByName(private val userRepository: UserRepository) {
    operator fun invoke(name: String) : UserProfile {
        return userRepository.getUserByUserName(name)
    }
}