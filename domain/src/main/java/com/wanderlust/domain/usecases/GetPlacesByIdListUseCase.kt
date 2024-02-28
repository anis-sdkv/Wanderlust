package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.Place
import com.wanderlust.domain.repositories.PlaceRepository

class GetPlacesByIdListUseCase(private val repository: PlaceRepository) {
    suspend operator fun invoke(ids: List<String>): List<Place> {
        return repository.getByIdArray(ids)
    }
}