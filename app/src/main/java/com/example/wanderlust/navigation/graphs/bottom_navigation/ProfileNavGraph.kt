package com.example.wanderlust.navigation.graphs.bottom_navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.wanderlust.navigation.BottomNavigationItem
import com.example.wanderlust.navigation.Graph
import com.example.wanderlust.navigation.graphs.authNavGraph
import com.example.wanderlust.ui.screens.edit_profile.EditProfileScreen
import com.example.wanderlust.ui.screens.profile.ProfileScreen

fun NavGraphBuilder.profileNavGraph(navController: NavHostController) {
    navigation(
        route = BottomNavigationItem.Profile.graph,
        startDestination = BottomNavigationItem.Profile.route
    ) {

        composable(route = BottomNavigationItem.Profile.route) {
            ProfileScreen(
                onNavigateToSignIn = {navController.navigate(Graph.AUTHENTICATION)},
                onNavigateToEditProfile = { navController.navigate(ProfileNavScreen.EditProfile.route)}
            )
        }

        composable(route = ProfileNavScreen.EditProfile.route) {
            EditProfileScreen()
        }
        authNavGraph(navController = navController)
    }

}

sealed class ProfileNavScreen(val route: String) {
    object EditProfile : ProfileNavScreen(route = "edit_profile")
}