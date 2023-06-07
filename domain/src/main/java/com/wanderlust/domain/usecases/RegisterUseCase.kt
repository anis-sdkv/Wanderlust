package com.wanderlust.domain.usecases

import com.wanderlust.domain.action_results.RegisterResult
import com.wanderlust.domain.services.UserService

class RegisterUseCase(private val userService: UserService) {
    suspend operator fun invoke(username: String, email: String, password: String): RegisterResult {
        return userService.register(username, email, password)
    }
}