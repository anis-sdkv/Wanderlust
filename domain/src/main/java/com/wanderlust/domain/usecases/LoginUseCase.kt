package com.wanderlust.domain.usecases

import com.wanderlust.domain.action_results.LoginResult
import com.wanderlust.domain.services.UserService

class LoginUseCase(private val userService: UserService) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        return userService.login(email, password)
    }
}