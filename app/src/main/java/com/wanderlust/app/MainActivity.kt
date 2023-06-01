package com.wanderlust.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wanderlust.app.utils.LocaleHelper
import com.wanderlust.domain.repositories.SettingsRepository
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.navigation.SetBottomNavigationBar
import com.wanderlust.ui.settings.LocalSettingsEventBus
import com.wanderlust.ui.settings.SettingsEventBus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository
    private val settingsEventBus = SettingsEventBus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val savedSettings = settingsRepository.get()
        settingsEventBus.updateDarkMode(savedSettings.isDarkMode)
        settingsEventBus.updateLanguage(savedSettings.language)

        setContent {
            val settingsEventBus = remember { settingsEventBus }
            val currentSettings = settingsEventBus.currentSettings.collectAsState().value
            LocaleHelper.setLocale(currentSettings.language.locale, resources)
            Firebase.auth.setLanguageCode(currentSettings.language.locale)

            WanderlustTheme(currentSettings.isDarkMode) {

                CompositionLocalProvider(LocalSettingsEventBus provides settingsEventBus) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SetBottomNavigationBar()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        settingsRepository.save(settingsEventBus.currentSettings.value)
    }
}