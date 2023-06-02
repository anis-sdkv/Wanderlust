package com.wanderlust.domain.usecases

import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.Place
import com.wanderlust.domain.repositories.CurrentUserRepository
import com.wanderlust.domain.repositories.PlaceRepository

class CreatePlaceUseCase(
    private val repository: PlaceRepository,
    private val currentUserRepository: CurrentUserRepository,
    private val getLocationByCoordinatesUseCase: GetLocationByCoordinatesUseCase
) {
    suspend operator fun invoke(place: Place): FirestoreActionResult {
        return try {
            val locationAddress = getLocationByCoordinatesUseCase(place.lat, place.lon)

            val user = currentUserRepository.get() ?: throw IllegalStateException()
            repository.create(
                user.id, place.copy(
                    city = locationAddress.city,
                    country = locationAddress.country,
                    authorId = user.id,
                )
            )
            currentUserRepository.notifyUpdated()
            FirestoreActionResult.SuccessResult
        } catch (e: Exception) {
            FirestoreActionResult.FailResult(e.message)
        }
    }
}