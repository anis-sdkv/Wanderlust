package com.wanderlust.ui.navigation.graphs.bottom_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.navigation
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.screens.home.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeNavGraph(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Home.graph,
        startDestination = BottomNavigationItem.Home.route
    ) {
        composable(route = BottomNavigationItem.Home.route) {
            bottomBarState.value = true
            HomeScreen()
        }
    }

}

sealed class HomeNavScreen(val route: String) {
    object Home : HomeNavScreen(route = "home_screen")

}