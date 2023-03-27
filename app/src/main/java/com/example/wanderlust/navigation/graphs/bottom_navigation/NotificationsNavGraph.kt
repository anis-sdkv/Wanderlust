package com.example.wanderlust.navigation.graphs.bottom_navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.wanderlust.navigation.BottomNavigationItem
import com.example.wanderlust.ui.screens.notifications.NotificationsScreen

fun NavGraphBuilder.notificationsNavGraph(navController: NavHostController) {
    navigation(
        route = BottomNavigationItem.Notifications.graph,
        startDestination = BottomNavigationItem.Notifications.route
    ) {
        composable(route = BottomNavigationItem.Notifications.route) {
            NotificationsScreen()
        }
    }

}

sealed class NotificationsNavScreen(val route: String) {
    object Notifications : NotificationsNavScreen(route = "notifications_screen")

}