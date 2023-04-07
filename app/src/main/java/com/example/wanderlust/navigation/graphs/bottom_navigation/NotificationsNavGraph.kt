package com.example.wanderlust.navigation.graphs.bottom_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.navigation
import com.example.wanderlust.navigation.BottomNavigationItem
import com.example.wanderlust.ui.screens.notifications.NotificationsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.notificationsNavGraph(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Notifications.graph,
        startDestination = BottomNavigationItem.Notifications.route
    ) {
        composable(route = BottomNavigationItem.Notifications.route) {
            bottomBarState.value = true
            NotificationsScreen()
        }
    }

}

sealed class NotificationsNavScreen(val route: String) {
    object Notifications : NotificationsNavScreen(route = "notifications_screen")

}