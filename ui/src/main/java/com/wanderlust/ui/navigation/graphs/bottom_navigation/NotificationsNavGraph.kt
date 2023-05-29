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
fun NavGraphBuilder.notificationsNavGraph(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Notifications.graph,
        startDestination = BottomNavigationItem.Notifications.route
    ) {
        composable(route = BottomNavigationItem.Notifications.route) {
            bottomBarState.value = true
            SettingsScreen(onNavigateBack = { navController.navigateUp() })
        }
    }

}

sealed class NotificationsNavScreen(val route: String) {
    object Notifications : NotificationsNavScreen(route = "notifications_screen")

}