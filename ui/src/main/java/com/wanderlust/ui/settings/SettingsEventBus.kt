package com.wanderlust.ui.settings

import androidx.compose.runtime.staticCompositionLocalOf
import com.wanderlust.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsEventBus {

    private val _currentSettings: MutableStateFlow<CurrentSettings> = MutableStateFlow(
        CurrentSettings(
            isDarkMode = false,
            language = WanderlustLanguage.RUSSIAN,
            currectUser = null
        )
    )

    val currentSettings: StateFlow<CurrentSettings> = _currentSettings

    fun updateDarkMode(isDarkMode: Boolean?) {
        _currentSettings.value = _currentSettings.value.copy(isDarkMode = isDarkMode)
    }

    fun updateLanguage(language: WanderlustLanguage) {
        _currentSettings.value = _currentSettings.value.copy(language = language)
    }

    fun updateUser(user: User) {
        _currentSettings.value = _currentSettings.value.copy(currectUser = user)
    }
}

val LocalSettingsEventBus = staticCompositionLocalOf {
    SettingsEventBus()
}