package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.Route
import com.wanderlust.domain.repositories.RouteRepository

class GetAllRoutesUseCase(private val repository: RouteRepository) {
    suspend operator fun invoke(): List<Route> {
        return repository.getAll()
    }
}