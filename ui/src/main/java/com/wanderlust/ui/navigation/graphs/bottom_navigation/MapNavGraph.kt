package com.wanderlust.ui.navigation.graphs.bottom_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.navigation
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.navigation.graphs.bottomNavGraph
import com.wanderlust.ui.screens.map.MapScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mapNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Map.graph,
        startDestination = BottomNavigationItem.Map.route
    ) {
        composable(route = BottomNavigationItem.Map.route) {
            isBottomBarVisible.value = true
            MapScreen(navController = navController)
        }
    }

}

sealed class MapNavScreen(val route: String) {
    object Map : MapNavScreen(route = "map_screen")

}