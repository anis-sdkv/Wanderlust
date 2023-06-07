package com.wanderlust.data.repositoriesImpl

import com.wanderlust.data.sources.local.sharedpref.AppPreferences
import com.wanderlust.data.sources.local.sharedpref.AppSettings
import com.wanderlust.domain.model.CurrentSettings
import com.wanderlust.domain.model.WanderlustLanguage
import com.wanderlust.domain.repositories.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val preferences: AppPreferences) : SettingsRepository {
    override fun save(settings: CurrentSettings) {
        preferences.saveSettings(AppSettings(settings.isDarkMode, settings.language.name))
    }

    override fun get(): CurrentSettings {
        val settings = preferences.getSettings()
        return CurrentSettings(settings.isDarkMode, WanderlustLanguage.valueOf(settings.language))
    }
}