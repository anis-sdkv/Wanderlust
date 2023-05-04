package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.User
import com.wanderlust.domain.repositories.UserRepository

class GetUserByName(userRepository: UserRepository) {

    private val userRepository = userRepository

    operator fun invoke(name: String) : User {
        return userRepository.getUserByUserName(name)
    }
}