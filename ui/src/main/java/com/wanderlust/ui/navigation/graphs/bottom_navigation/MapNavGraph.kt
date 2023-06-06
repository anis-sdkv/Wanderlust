package com.wanderlust.ui.navigation.graphs.bottom_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.screens.map.MapScreen
import com.wanderlust.ui.screens.search.SearchScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mapNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Map.graph,
        startDestination = MapNavScreen.Map.route
    ) {
        composable(
            route = MapNavScreen.Map.route,
            arguments = listOf(
                navArgument("searchValue") {
                    type = NavType.StringType
                },
                navArgument("searchType") {
                    type = NavType.BoolType
                },
                navArgument("searchTags") {
                    type = NavType.StringType
                }
            )
        ) {
            isBottomBarVisible.value = true
            MapScreen(navController = navController)
        }
        composable(
            route = MapNavScreen.Search.route,
            arguments = listOf(
                navArgument("searchValue") {
                    type = NavType.StringType
                },
                navArgument("screenName") {
                    type = NavType.StringType
                }
            )
        ) {
            isBottomBarVisible.value = true
            SearchScreen(
                navController = navController,
                it.arguments?.getString("screenName")
            )
        }
    }

}

sealed class MapNavScreen(val route: String) {

    object Map : MapNavScreen(route = "map_screen/{$SEARCH_VALUE_KEY}/{$SEARCH_BY_NAME_KEY}/{$SEARCH_TAGS_KEY}"){
        fun passValues(searchValue: String, searchType: Boolean, searchTags: String): String {
            return "map_screen/$searchValue/$searchType/$searchTags"
        }
    }

    object Search : MapNavScreen(route = "map_search_screen/{$SEARCH_VALUE_KEY}/{$SEARCH_SCREEN_KEY}"){
        fun passSearchValue(searchValue: String, screenName: String): String {
            return "map_search_screen/$searchValue/$screenName"
        }
    }
}