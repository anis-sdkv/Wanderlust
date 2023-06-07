package com.wanderlust.domain.repositories

import com.wanderlust.domain.model.CurrentSettings

interface SettingsRepository {
    fun save(settings: CurrentSettings)
    fun get(): CurrentSettings
}