package com.wanderlust.domain.usecases

import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.CurrentUserRepository
import com.wanderlust.domain.repositories.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository,
    private val currentUserRepository: CurrentUserRepository
) {
    suspend operator fun invoke(userProfile: UserProfile): FirestoreActionResult =
        try {
            userRepository.update(userProfile)
            currentUserRepository.notifyUpdated()
            FirestoreActionResult.SuccessResult
        } catch (e: Exception) {
            FirestoreActionResult.FailResult(e.message)
        }

}