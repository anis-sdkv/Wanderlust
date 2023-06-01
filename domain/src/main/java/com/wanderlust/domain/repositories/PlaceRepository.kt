package com.wanderlust.domain.repositories

import com.wanderlust.domain.model.Place

interface PlaceRepository {
    suspend fun getByIdArray(ids: List<String>): List<Place>
    suspend fun getById(id: String): Place?
}