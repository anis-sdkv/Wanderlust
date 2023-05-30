package com.wanderlust.ui.navigation.graphs

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.navigation.graphs.bottom_navigation.homeNavGraph
import com.wanderlust.ui.navigation.graphs.bottom_navigation.mapNavGraph
import com.wanderlust.ui.navigation.graphs.bottom_navigation.notificationsNavGraph
import com.wanderlust.ui.navigation.graphs.bottom_navigation.profileNavGraph
import com.wanderlust.ui.screens.create_place.CreatePlaceScreen
import com.wanderlust.ui.screens.create_route.CreateRouteScreen


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    AnimatedNavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.BOTTOM,
        modifier = Modifier
            .systemBarsPadding()
            .navigationBarsPadding()) {

        bottomNavGraph(navController = navController, isBottomBarVisible)
    }
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.bottomNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = Graph.BOTTOM,
        startDestination = BottomNavigationItem.Home.graph
    ) {

        //Графы к каждой вкладке Bottom Navigation:
        homeNavGraph(navController = navController, isBottomBarVisible)
        mapNavGraph(navController = navController, isBottomBarVisible)
        notificationsNavGraph(navController = navController, isBottomBarVisible)
        profileNavGraph(navController = navController, isBottomBarVisible)


        //authNavGraph(navController = navController)

        // Переход к экранам от BottomSheet, возможно потом
        // нужно будет сделать отдельные графы?
        composable(
            BottomSheetScreen.CreatePlace.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
            }
        ){
            isBottomBarVisible.value = false
            CreatePlaceScreen(navController = navController)
        }
        composable(
            BottomSheetScreen.CreateRoute.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
            }
        ){
            isBottomBarVisible.value = false
            CreateRouteScreen(navController = navController)
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

