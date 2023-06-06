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
import com.wanderlust.ui.navigation.graphs.Graph
import com.wanderlust.ui.navigation.graphs.authNavGraph
import com.wanderlust.ui.screens.edit_profile.EditProfileScreen
import com.wanderlust.ui.screens.profile.ProfileScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.profileNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Profile.graph,
        startDestination = ProfileNavScreen.Profile.route
    ) {

        composable(
            route = ProfileNavScreen.Profile.route,
            arguments = listOf(
                navArgument(ProfileNavScreen.USER_ID_KEY) {
                    type = NavType.StringType
                    defaultValue = ProfileNavScreen.SELF_PROFILE
                }
            )
        ) {
            isBottomBarVisible.value = true
            ProfileScreen(navController)
        }

        composable(
            route = ProfileNavScreen.EditProfile.route,
            arguments = listOf(
                navArgument(ProfileNavScreen.USER_ID_KEY) {
                    type = NavType.StringType
                    defaultValue = ProfileNavScreen.SELF_PROFILE
                }
            )
        ) {
            isBottomBarVisible.value = true
            EditProfileScreen(navController = navController)
        }
        authNavGraph(navController = navController, isBottomBarVisible)
    }

}

sealed class ProfileNavScreen(val route: String) {
    object EditProfile : ProfileNavScreen(route = "edit_profile/$USER_ID_KEY") {
        fun passUserId(id: String) = "edit_profile/$id"
    }

    object Profile : ProfileNavScreen(route = "profile/{$USER_ID_KEY}") {
        fun passUserId(id: String) = "profile/$id"
    }

    companion object {
        const val USER_ID_KEY = "userId"
        const val SELF_PROFILE = "self"
    }
}