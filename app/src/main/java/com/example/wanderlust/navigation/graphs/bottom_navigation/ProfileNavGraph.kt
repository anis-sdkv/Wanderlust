package com.example.wanderlust.navigation.graphs.bottom_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.navigation
import com.example.wanderlust.navigation.BottomNavigationItem
import com.example.wanderlust.navigation.Graph
import com.example.wanderlust.navigation.graphs.authNavGraph
import com.example.wanderlust.ui.screens.edit_profile.EditProfileScreen
import com.example.wanderlust.ui.screens.profile.ProfileScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.profileNavGraph(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Profile.graph,
        startDestination = BottomNavigationItem.Profile.route
    ) {

        composable(route = BottomNavigationItem.Profile.route) {
            bottomBarState.value = true
            ProfileScreen(
                onNavigateToSignIn = {navController.navigate(Graph.AUTHENTICATION)},
                onNavigateToEditProfile = { navController.navigate(ProfileNavScreen.EditProfile.route)}
            )
        }

        composable(route = ProfileNavScreen.EditProfile.route) {
            bottomBarState.value = true
            EditProfileScreen()
        }
        authNavGraph(navController = navController, bottomBarState)
    }

}

sealed class ProfileNavScreen(val route: String) {
    object EditProfile : ProfileNavScreen(route = "edit_profile")
}