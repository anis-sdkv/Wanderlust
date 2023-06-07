package com.wanderlust.domain.usecases

import com.wanderlust.domain.repositories.CurrentUserRepository

class SignOutUseCase(private val repository: CurrentUserRepository) {
    operator fun invoke() {
        repository.signOut()
    }
}