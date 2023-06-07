package com.wanderlust.domain.services

import com.wanderlust.domain.model.LocationAddress

interface GeocoderService {
    suspend fun decodeByCoordinates(lat: Double, lon: Double, language: String): LocationAddress
}