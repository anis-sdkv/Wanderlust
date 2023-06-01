package com.wanderlust.domain.usecases

import com.wanderlust.domain.action_results.UpdateResult
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.CurrentUserRepository
import com.wanderlust.domain.repositories.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository,
    private val currentUserRepository: CurrentUserRepository
) {
    suspend operator fun invoke(userProfile: UserProfile): UpdateResult =
        try {
            userRepository.update(userProfile)
            currentUserRepository.notifyUpdated()
            UpdateResult.SuccessResult
        } catch (e: Exception) {
            UpdateResult.FailResult(e.message)
        }

}