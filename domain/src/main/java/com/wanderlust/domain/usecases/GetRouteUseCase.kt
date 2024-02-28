package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.Route
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.RouteRepository

class GetRouteUseCase(private val routeRepository: RouteRepository) {
    suspend operator fun invoke(id: String) : Route? {
        return routeRepository.getById(id)
    }
}