package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.Place
import com.wanderlust.domain.repositories.PlaceRepository

class GetAllPlacesUseCase(private val repository: PlaceRepository) {
    suspend operator fun invoke(): List<Place> {
        return repository.getAll()
    }
}