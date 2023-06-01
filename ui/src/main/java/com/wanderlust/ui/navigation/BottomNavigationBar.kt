package com.wanderlust.ui.navigation


import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wanderlust.ui.navigation.graphs.RootNavGraph
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(navController: NavController, isBottomBarVisible: MutableState<Boolean>) {
    val items = listOf(
        BottomNavigationItem.Home,
        BottomNavigationItem.Map,
        BottomNavigationItem.Notifications,
        BottomNavigationItem.Profile
    )

    AnimatedVisibility(
        visible = isBottomBarVisible.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavigationBar(
            modifier = Modifier.height(64.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp) ),
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    modifier = Modifier.offset(
                        when (item.title) {
                            "Notifications" -> 20.dp
                            "Map" -> (-20).dp
                            else -> 0.dp
                        }
                    ),
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier.size(27.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Black.copy(0.4f),
                        indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    alwaysShowLabel = false,
                    selected = currentDestination?.hierarchy?.any { it.route == item.graph } == true,
                    onClick = {

                        navController.navigate(item.graph) {
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
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetBottomNavigationBar() {
    val navController = rememberAnimatedNavController()
    val fabShape = RoundedCornerShape(50)
    val scope = rememberCoroutineScope()

    val isBottomBarVisible = rememberSaveable { (mutableStateOf(true)) }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // BottomSheet content:
    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier,
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = {
                        navController.navigate("create_place")
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                openBottomSheet = false
                            }
                        }
                    },
                    border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.bottom_sheet_ic_place),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.add_place),
                        style = WanderlustTheme.typography.bold16,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                OutlinedButton(
                    onClick = {
                        navController.navigate("create_route")
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                openBottomSheet = false
                            }
                        }
                    },
                    border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.bottom_sheet_ic_route),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.add_route),
                        style = WanderlustTheme.typography.bold16,
                    )
                }
            }
        }
    }

    // BottomBar content:

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                AnimatedVisibility(
                    visible = isBottomBarVisible.value,
                    enter = slideInVertically(initialOffsetY = { 270 }),
                    exit = slideOutVertically(targetOffsetY = { 270 }),
                ) {
                    BottomNavigationBar(navController = navController, isBottomBarVisible = isBottomBarVisible)
                }
            }
        },

        floatingActionButton = {
            AnimatedVisibility(
                visible = isBottomBarVisible.value,
                enter = slideInVertically(initialOffsetY = { 270 }),
                exit = slideOutVertically(targetOffsetY = { 270 }),
            ) {
                FloatingActionButton(
                    modifier = Modifier.offset(0.dp, 40.dp),
                    onClick = {
                        openBottomSheet = !openBottomSheet
                    },
                    shape = fabShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Rounded.Add, "", modifier = Modifier.size(40.dp))
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { value ->
            RootNavGraph(navController = navController, isBottomBarVisible)
        }
    )
}
