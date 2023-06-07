package com.wanderlust.domain.repositories

import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.model.Place

interface PlaceRepository {
    suspend fun getByIdArray(ids: List<String>): List<Place>
    suspend fun getById(id: String): Place?
    suspend fun create(userId: String, place: Place)
    suspend fun getAll(): List<Place>
    suspend fun addComment(placeId: String, comment: Comment)
}