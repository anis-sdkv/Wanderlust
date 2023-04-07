package com.example.wanderlust.navigation.graphs

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.navigation
import com.example.wanderlust.navigation.Graph
import com.example.wanderlust.ui.screens.sign_in.SignInScreen
import com.example.wanderlust.ui.screens.sign_up.SignUpScreen

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("ComposableNavGraphInComposeScope")
fun NavGraphBuilder.authNavGraph(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.SignIn.route
    ) {
        composable(route = AuthScreen.SignIn.route) {
            bottomBarState.value = false
            SignInScreen(onNavigateToSignUp = {
                navController.navigate(AuthScreen.SignUp.route){
                    navController.graph.route?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }
        composable(route = AuthScreen.SignUp.route) {
            bottomBarState.value = false
            SignUpScreen(onNavigateToSignIn = {
                navController.navigate(AuthScreen.SignIn.route){
                    navController.graph.route?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }
        composable(route = AuthScreen.Forgot.route) {
            //TODO
        }
    }

}

sealed class AuthScreen(val route: String) {
    object SignIn : AuthScreen(route = "sign_in")
    object SignUp : AuthScreen(route = "sign_up")
    object Forgot : AuthScreen(route = "forgot") //TODO
}