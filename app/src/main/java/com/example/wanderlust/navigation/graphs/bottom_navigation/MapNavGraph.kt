package com.example.wanderlust.navigation.graphs.bottom_navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.wanderlust.navigation.BottomNavigationItem
import com.example.wanderlust.ui.screens.map.MapScreen

fun NavGraphBuilder.mapNavGraph(navController: NavHostController) {
    navigation(
        route = BottomNavigationItem.Map.graph,
        startDestination = BottomNavigationItem.Map.route
    ) {
        composable(route = BottomNavigationItem.Map.route) {
            MapScreen()
        }
    }

}

sealed class MapNavScreen(val route: String) {
    object Map : MapNavScreen(route = "map_screen")

}