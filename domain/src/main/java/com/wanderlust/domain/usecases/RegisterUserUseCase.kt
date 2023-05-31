package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.RegisterResult
import com.wanderlust.domain.services.UserService

class RegisterUserUseCase(private val userService: UserService) {

    suspend operator fun invoke(username: String, email: String, password: String): RegisterResult {
        return userService.register(username, email, password)
    }
}