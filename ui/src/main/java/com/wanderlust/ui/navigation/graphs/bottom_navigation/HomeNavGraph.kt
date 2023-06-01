package com.wanderlust.ui.navigation.graphs.bottom_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.animation.composable
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.screens.home.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Home.graph,
        startDestination = HomeNavScreen.Home.route
    ) {
        composable(route = HomeNavScreen.Home.route) {
            isBottomBarVisible.value = true
            HomeScreen()
        }
    }

}

sealed class HomeNavScreen(val route: String) {
    object Home : HomeNavScreen(route = "home_screen")

}