package com.wanderlust.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.MapEntityCard
import com.wanderlust.ui.components.common.SearchTextField
import com.wanderlust.ui.components.common.SquareButton
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.navigation.graphs.bottom_navigation.HomeNavScreen
import com.wanderlust.ui.screens.home.SortCategory.ALL_PLACES
import com.wanderlust.ui.screens.home.SortCategory.ALL_ROUTES
import com.wanderlust.ui.screens.home.SortCategory.FAVOURITE_ROUTES
import com.wanderlust.ui.utils.calculateRating

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(Unit) {
        eventHandler.invoke(HomeEvent.OnLaunch)
    }
    DisposableEffect(Unit) {
        onDispose {
            eventHandler.invoke(HomeEvent.OnDispose)
        }
    }

    LaunchedEffect(action) {
        when (action) {
            HomeSideEffect.NavigateToSearchScreen -> {
                navController.navigate(
                    HomeNavScreen.Search.passSearchValue(
                        if (state.searchValue == "") "empty" else state.searchValue,
                        "home"
                    )
                )
            }

            is HomeSideEffect.NavigateToRouteScreen -> {
                navController.navigate(HomeNavScreen.Route.passRouteId((action as HomeSideEffect.NavigateToRouteScreen).id))
            }

            is HomeSideEffect.NavigateToPlaceScreen -> {
                navController.navigate(HomeNavScreen.Place.passPlaceId((action as HomeSideEffect.NavigateToPlaceScreen).id))
            }

            null -> Unit
        }
    }
    
    Column(
        modifier = Modifier
            .background(WanderlustTheme.colors.primaryBackground)
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 64.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            SearchTextField(
                modifier = Modifier.weight(1f),
                searchValue = state.searchValue,
                onChange = { searchValue -> eventHandler.invoke(HomeEvent.OnSearchValueChanged(searchValue)) }
            )
            SquareButton(
                icon = R.drawable.ic_filter,
                iconColor = WanderlustTheme.colors.secondaryText,
                backgroundColor = WanderlustTheme.colors.solid,
                onClick = { eventHandler.invoke(HomeEvent.OnFilterBtnClick) }
            )
            SquareButton(
                icon = R.drawable.ic_search,
                iconColor = WanderlustTheme.colors.onAccent,
                backgroundColor = WanderlustTheme.colors.accent,
                onClick = { eventHandler.invoke(HomeEvent.OnSearchBtnClick) }
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 20.dp)
                .height(40.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .clickable { eventHandler.invoke(HomeEvent.OnCategoryClick(ALL_ROUTES)) },
                    text = stringResource(id = R.string.all_routes),
                    style = WanderlustTheme.typography.bold20,
                    color = if (state.selectedCategory == ALL_ROUTES) WanderlustTheme.colors.primaryText else WanderlustTheme.colors.secondaryText
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .clickable { eventHandler.invoke(HomeEvent.OnCategoryClick(ALL_PLACES)) },
                    text = stringResource(id = R.string.all_places),
                    style = WanderlustTheme.typography.bold20,
                    color = if (state.selectedCategory == ALL_PLACES) WanderlustTheme.colors.primaryText else WanderlustTheme.colors.secondaryText
                )

                Text(
                    modifier = Modifier
                        .clickable { eventHandler.invoke(HomeEvent.OnCategoryClick(FAVOURITE_ROUTES)) },
                    text = stringResource(id = R.string.favourites),
                    style = WanderlustTheme.typography.bold20,
                    color = if (state.selectedCategory == FAVOURITE_ROUTES) WanderlustTheme.colors.primaryText else WanderlustTheme.colors.secondaryText
                )

                Spacer(modifier = Modifier.width(120.dp))
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(160.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                WanderlustTheme.colors.primaryBackground
                            )
                        )
                    )
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = WanderlustTheme.colors.accent)
            }
            return
        }

        when (state.selectedCategory) {
            ALL_ROUTES -> {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(state.routes.size) {
                        MapEntityCard(
                            name = state.routes[it].routeName,
                            rating = state.routes[it].calculateRating(),
                            onClick = {
                                eventHandler.invoke(
                                    HomeEvent.OnRouteCardClick(
                                        state.routes[it].id ?: throw IllegalArgumentException()
                                    )
                                )
                            }
                        )
                    }
                }
            }

            ALL_PLACES -> {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(state.places.size) {
                        MapEntityCard(
                            name = state.places[it].placeName,
                            rating = state.places[it].calculateRating(),
                            onClick = {
                                eventHandler.invoke(
                                    HomeEvent.OnPlaceCardClick(
                                        state.places[it].id ?: throw IllegalArgumentException()
                                    )
                                )
                            }
                        )
                    }
                }
            }

            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "TODO",
                        style = WanderlustTheme.typography.bold20,
                        color = WanderlustTheme.colors.secondaryText
                    )
                }
            }
        }
    }
}
