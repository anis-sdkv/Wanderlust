package com.example.wanderlust.navigation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wanderlust.navigation.graphs.AuthScreen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem.Home,
        BottomNavigationItem.Map,
        BottomNavigationItem.Notifications,
        BottomNavigationItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier.height(64.dp),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ){


        items.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.offset(
                    when (item.title){
                        "Notifications" -> 20.dp
                        "Map" -> (-20).dp
                        else -> 0.dp
                    }
                ),
                icon = { Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                    modifier = Modifier.size(27.dp))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Black.copy(0.4f),
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                alwaysShowLabel = false,
                selected = currentDestination?.hierarchy?.any { it.route == item.route || it.route == item.graph} == true,
                onClick = {

                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun SetBottomNavigationBar(){
    val navController = rememberNavController()
    val fabShape = RoundedCornerShape(50)

    val screensWithoutBottomBar = listOf(
        AuthScreen.SignUp,
        AuthScreen.SignIn
    )
    val hideBottomBar = navController
        .currentBackStackEntryAsState().value?.destination?.route in screensWithoutBottomBar.map { it.route }

    Scaffold(
        bottomBar = {
            if(!hideBottomBar)
                BottomNavigationBar(navController = navController)
                    },

        floatingActionButton = {
            if(!hideBottomBar) {
            FloatingActionButton(
                modifier = Modifier.offset(0.dp, 40.dp),
                onClick = {
                    //TODO
                },
                shape = fabShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Rounded.Add,"", modifier = Modifier.size(40.dp))
            }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        content = { padding ->
            Box(Modifier.padding(bottom = padding.calculateBottomPadding())
                ) {
                RootNavGraph(navController = navController)
            }
        }
    )
}
