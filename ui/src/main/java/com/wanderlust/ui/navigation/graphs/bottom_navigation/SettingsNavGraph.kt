package com.wanderlust.ui.navigation.graphs.bottom_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.animation.composable
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.screens.settings.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Settins.graph,
        startDestination = SettingsNavScreen.Settings.route
    ) {
        composable(route = SettingsNavScreen.Settings.route) {
            isBottomBarVisible.value = true
            SettingsScreen(navController = navController)
        }
    }

}

sealed class SettingsNavScreen(val route: String) {
    object Settings : SettingsNavScreen(route = "settings")

}