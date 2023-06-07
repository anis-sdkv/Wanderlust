package com.wanderlust.domain.usecases

import com.wanderlust.domain.repositories.CurrentUserRepository

class SetCurrentUserUseCase(private val repository: CurrentUserRepository) {
    operator fun invoke(id: String) {
        repository.set(id)
    }
}