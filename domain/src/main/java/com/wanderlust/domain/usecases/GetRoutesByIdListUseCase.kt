package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.Route
import com.wanderlust.domain.repositories.RouteRepository

class GetRoutesByIdListUseCase(private val repository: RouteRepository) {
    suspend operator fun invoke(ids: List<String>): List<Route> {
        return repository.getByIdArray(ids)
    }
}