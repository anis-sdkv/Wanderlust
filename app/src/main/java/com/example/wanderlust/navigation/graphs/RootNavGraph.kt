package com.example.wanderlust.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.example.wanderlust.navigation.graphs.*
import com.example.wanderlust.navigation.graphs.bottom_navigation.homeNavGraph
import com.example.wanderlust.navigation.graphs.bottom_navigation.mapNavGraph
import com.example.wanderlust.navigation.graphs.bottom_navigation.notificationsNavGraph
import com.example.wanderlust.navigation.graphs.bottom_navigation.profileNavGraph
import com.example.wanderlust.ui.screens.create_place.CreatePlaceScreen
import com.example.wanderlust.ui.screens.create_route.CreateRouteScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavGraph(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    AnimatedNavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.BOTTOM,
        modifier = Modifier
            .systemBarsPadding()
            .navigationBarsPadding()) {

        bottomNavGraph(navController = navController, bottomBarState)
    }
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.bottomNavGraph(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    navigation(
        route = Graph.BOTTOM,
        startDestination = BottomNavigationItem.Home.graph
    ) {

        //Графы к каждой вкладке Bottom Navigation:
        homeNavGraph(navController = navController, bottomBarState)
        mapNavGraph(navController = navController, bottomBarState)
        notificationsNavGraph(navController = navController, bottomBarState)
        profileNavGraph(navController = navController, bottomBarState)


        //authNavGraph(navController = navController)

        // Переход к экранам от BottomSheet, возможно потом
        // нужно будет сделать отдельные графы?
        composable(BottomSheetScreen.CreatePlace.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
            }
        ){
            bottomBarState.value = false
            CreatePlaceScreen()
        }
        composable(BottomSheetScreen.CreateRoute.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
            }
        ){
            bottomBarState.value = false
            CreateRouteScreen()
        }
    }

}

sealed class BottomSheetScreen(val route: String) {
    object CreatePlace : BottomSheetScreen(route = "create_place")
    object CreateRoute : BottomSheetScreen(route = "create_route")
}

object Graph {
    const val AUTHENTICATION = "auth_graph"
    const val BOTTOM = "bottom_graph"
    const val ROOT = "root_graph"
}

