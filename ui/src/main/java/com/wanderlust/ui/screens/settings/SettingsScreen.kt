package com.wanderlust.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.ScreenHeader
import com.wanderlust.ui.components.common.SelectableItem
import com.wanderlust.ui.components.common.Selector
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.settings.LocalSettingsEventBus
import com.wanderlust.ui.settings.WanderlustLanguage

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val settingsEventBus = LocalSettingsEventBus.current
    val currentSettings = settingsEventBus.currentSettings.collectAsState().value

    val themes = listOf(
        SelectableItem(stringResource(id = R.string.light)) { settingsEventBus.updateDarkMode(false) },
        SelectableItem(stringResource(id = R.string.dark)) { settingsEventBus.updateDarkMode(true) },
        SelectableItem(stringResource(id = R.string.system)) { settingsEventBus.updateDarkMode(null) }
    )

    val languages = listOf(
        SelectableItem(stringResource(id = R.string.english)) { settingsEventBus.updateLanguage(WanderlustLanguage.ENLISH) },
        SelectableItem(stringResource(id = R.string.russian)) { settingsEventBus.updateLanguage(WanderlustLanguage.RUSSIAN) }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WanderlustTheme.colors.primaryBackground)
            .padding(top = 48.dp)
            .padding(horizontal = 20.dp)

    ) {

        ScreenHeader(screenName = stringResource(id = R.string.settings)) { onNavigateBack() }

        Spacer(modifier = Modifier.height(32.dp))
        Selector(
            label = stringResource(id = R.string.theme),
            items = themes,
            initIndex = if (currentSettings.isDarkMode == null) 2 else if (currentSettings.isDarkMode) 1 else 0
        )

        Spacer(modifier = Modifier.height(32.dp))
        Selector(
            label = stringResource(id = R.string.language),
            items = languages,
            initIndex = if (currentSettings.language == WanderlustLanguage.ENLISH) 0 else 1
        )
    }
}