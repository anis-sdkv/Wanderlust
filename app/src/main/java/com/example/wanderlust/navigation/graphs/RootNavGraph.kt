package com.example.wanderlust.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.example.wanderlust.navigation.graphs.*
import com.example.wanderlust.navigation.graphs.bottom_navigation.homeNavGraph
import com.example.wanderlust.navigation.graphs.bottom_navigation.mapNavGraph
import com.example.wanderlust.navigation.graphs.bottom_navigation.notificationsNavGraph
import com.example.wanderlust.navigation.graphs.bottom_navigation.profileNavGraph


@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.BOTTOM) {

        bottomNavGraph(navController = navController)

        authNavGraph(navController = navController)
    }
}


fun NavGraphBuilder.bottomNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.BOTTOM,
        startDestination = BottomNavigationItem.Home.graph
    ) {

        //Графы к каждой вкладке Bottom Navigation:
        homeNavGraph(navController = navController)
        mapNavGraph(navController = navController)
        notificationsNavGraph(navController = navController)
        profileNavGraph(navController = navController)

        authNavGraph(navController = navController)
    }

}



object Graph {
    const val AUTHENTICATION = "auth_graph"
    const val BOTTOM = "bottom_graph"
    const val ROOT = "root_graph"
}

