package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.Place
import com.wanderlust.domain.repositories.PlaceRepository

class GetPlaceUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(id: String): Place? {
        return placeRepository.getById(id)
    }
}