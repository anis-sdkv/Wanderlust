package com.wanderlust.domain.usecases

import com.wanderlust.domain.repositories.CurrentUserRepository

class GetCurrentUserIdUseCase(private val repository: CurrentUserRepository) {
    operator fun invoke(): String? {
        return repository.getId()
    }
}