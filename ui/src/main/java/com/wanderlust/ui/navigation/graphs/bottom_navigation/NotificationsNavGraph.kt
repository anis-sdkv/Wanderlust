package com.wanderlust.ui.navigation.graphs.bottom_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.animation.composable
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.navigation.graphs.Graph
import com.wanderlust.ui.screens.place.PlaceScreen
import com.wanderlust.ui.screens.route.RouteScreen
import com.wanderlust.ui.screens.settings.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.notificationsNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Notifications.graph,
        startDestination = NotificationsNavScreen.Route.route
    ) {
        composable(route = NotificationsNavScreen.Notifications.route) {
            isBottomBarVisible.value = true
            SettingsScreen(navController = navController)
        }
        composable(route = NotificationsNavScreen.Route.route) {
            isBottomBarVisible.value = true
            RouteScreen(navController = navController)
        }
        composable(route = NotificationsNavScreen.Place.route) {
            isBottomBarVisible.value = true
            PlaceScreen(navController = navController)
        }
    }

}

sealed class NotificationsNavScreen(val route: String) {
    object Notifications : NotificationsNavScreen(route = "notifications_screen")
    object Route : NotificationsNavScreen(route = "route_screen")
    object Place : NotificationsNavScreen(route = "place_screen")

}