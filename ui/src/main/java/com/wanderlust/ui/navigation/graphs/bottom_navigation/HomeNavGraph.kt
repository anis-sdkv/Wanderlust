package com.wanderlust.ui.navigation.graphs.bottom_navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.screens.home.HomeScreen
import com.wanderlust.ui.screens.search.SearchScreen


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Home.graph,
        startDestination = HomeNavScreen.Home.route
    ) {
        composable(
            route = HomeNavScreen.Home.route,
            arguments = listOf(
                navArgument("searchValue") {
                    type = NavType.StringType
                },
                navArgument("searchType") {
                    type = NavType.BoolType
                },
                /*navArgument("searchTags") {
                    type = TagsType()
                }*/
            )
        ) {
            isBottomBarVisible.value = true
            HomeScreen(
                navController = navController,
            )
        }
        composable(
            route = HomeNavScreen.Search.route,
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

const val SEARCH_VALUE_KEY = "searchValue"
const val SEARCH_TYPE_KEY = "searchType"
const val SEARCH_TAGS_KEY = "searchTags"
const val SEARCH_SCREEN_KEY = "screenName"
sealed class HomeNavScreen(val route: String) {

    object Home : HomeNavScreen(route = "home_screen/{$SEARCH_VALUE_KEY}/{$SEARCH_TYPE_KEY}"){
        fun passValues(searchValue: String, searchType: Boolean): String {
            return "home_screen/$searchValue/$searchType"
        }
    }

    /*object Home : HomeNavScreen(route = "home_screen/{$SEARCH_VALUE_KEY}/{$SEARCH_TYPE_KEY}/{$SEARCH_TAGS_KEY}"){
        fun passValues(searchValue: String, searchType: Boolean, searchTags: String): String {
            return "home_screen/$searchValue/$searchType/$searchTags"
        }
    }*/

    object Search : HomeNavScreen(route = "home_search_screen/{$SEARCH_VALUE_KEY}/{$SEARCH_SCREEN_KEY}"){
        fun passSearchValue(searchValue: String, screenName: String): String {
            return "home_search_screen/$searchValue/$screenName"
        }
    }
}