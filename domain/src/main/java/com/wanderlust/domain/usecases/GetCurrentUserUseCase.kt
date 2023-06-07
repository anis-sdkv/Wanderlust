package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.CurrentUserRepository

class GetCurrentUserUseCase(private val repository: CurrentUserRepository) {
    suspend operator fun invoke() : UserProfile? {
        return repository.get()
    }
}