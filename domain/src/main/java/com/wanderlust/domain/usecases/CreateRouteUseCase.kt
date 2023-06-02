package com.wanderlust.domain.usecases

import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.Place
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.repositories.CurrentUserRepository
import com.wanderlust.domain.repositories.PlaceRepository
import com.wanderlust.domain.repositories.RouteRepository

class CreateRouteUseCase (
    private val repository: RouteRepository,
    private val currentUserRepository: CurrentUserRepository,
    private val getLocationByCoordinatesUseCase: GetLocationByCoordinatesUseCase
) {
    suspend operator fun invoke(route: Route): FirestoreActionResult {
        return try {
            val firstPoint = route.points.first()
            val locationAddress = getLocationByCoordinatesUseCase(firstPoint.lat, firstPoint.lon)

            val user = currentUserRepository.get() ?: throw IllegalStateException()
            repository.create(
                user.id, route.copy(
                    city = locationAddress.city,
                    country = locationAddress.country
                )
            )
            FirestoreActionResult.SuccessResult
        } catch (e: Exception) {
            FirestoreActionResult.FailResult(e.message)
        }
    }
}