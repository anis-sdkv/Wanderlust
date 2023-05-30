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
        startDestination = BottomNavigationItem.Profile.route
    ) {

        composable(
            route = BottomNavigationItem.Profile.route,
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                    defaultValue = "myProfile"
                }
            )
        ) {
            isBottomBarVisible.value = true
            ProfileScreen(
                onNavigateToSignIn = {navController.navigate(Graph.AUTHENTICATION)},
                onNavigateToEditProfile = { navController.navigate(ProfileNavScreen.EditProfile.route)},
            )
        }

        composable(
            route = ProfileNavScreen.EditProfile.route,
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                    defaultValue = "myProfile"
                }
            )
        ) {
            isBottomBarVisible.value = true
            EditProfileScreen(onNavigateBack = {navController.navigateUp()})
        }
        authNavGraph(navController = navController, isBottomBarVisible)
    }

}

sealed class ProfileNavScreen(val route: String) {
    object EditProfile : ProfileNavScreen(route = "edit_profile?userName={userName}")
}