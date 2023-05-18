package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.User
import com.wanderlust.domain.repositories.UserRepository

class GetUserByName (private val userRepository: UserRepository) {

    operator fun invoke(name: String) : User {
        return userRepository.getUserByUserName(name)
    }
}