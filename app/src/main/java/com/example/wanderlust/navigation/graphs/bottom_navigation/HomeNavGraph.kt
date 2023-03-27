package com.example.wanderlust.navigation.graphs.bottom_navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.wanderlust.navigation.BottomNavigationItem
import com.example.wanderlust.ui.screens.home.HomeScreen

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation(
        route = BottomNavigationItem.Home.graph,
        startDestination = BottomNavigationItem.Home.route
    ) {
        composable(route = BottomNavigationItem.Home.route) {
            HomeScreen()
        }
    }

}

sealed class HomeNavScreen(val route: String) {
    object Home : HomeNavScreen(route = "home_screen")

}