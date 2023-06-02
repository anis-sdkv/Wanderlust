package com.wanderlust.domain.usecases

import com.wanderlust.domain.model.LocationAddress
import com.wanderlust.domain.repositories.SettingsRepository
import com.wanderlust.domain.services.GeocoderService

class GetLocationByCoordinatesUseCase(
    private val service: GeocoderService,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): LocationAddress {
        val locale = settingsRepository.get().language.locale
        return service.decodeByCoordinates(lat, lon, locale)
    }
}