package com.wanderlust.ui.navigation.graphs

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.animation.composable
import com.wanderlust.ui.screens.sign_in.SignInScreen
import com.wanderlust.ui.screens.sign_up.SignUpScreen

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("ComposableNavGraphInComposeScope")
fun NavGraphBuilder.authNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = com.wanderlust.ui.navigation.graphs.AuthScreen.SignIn.route
    ) {
        composable(route = com.wanderlust.ui.navigation.graphs.AuthScreen.SignIn.route) {
            isBottomBarVisible.value = false
            SignInScreen(navController)
        }
        composable(route = AuthScreen.SignUp.route) {
            isBottomBarVisible.value = false
            SignUpScreen(
                navController
            )
        }
        composable(route = com.wanderlust.ui.navigation.graphs.AuthScreen.Forgot.route) {
            //TODO
        }
    }

}

sealed class AuthScreen(val route: String) {
    object SignIn : AuthScreen(route = "sign_in")
    object SignUp : AuthScreen(route = "sign_up")
    object Forgot : AuthScreen(route = "forgot") //TODO
}