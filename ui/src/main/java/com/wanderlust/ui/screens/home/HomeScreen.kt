package com.wanderlust.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.SearchTextField
import com.wanderlust.ui.components.common.SquareButton
import com.wanderlust.ui.custom.WanderlustTheme
//import com.wanderlust.ui.navigation.graphs.SearchNavScreen
import com.wanderlust.ui.navigation.graphs.bottom_navigation.HomeNavScreen

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val homeState by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            null -> Unit
            HomeSideEffect.NavigateToSearchScreen -> {
                navController.navigate(
                    HomeNavScreen.Search.passSearchValue(
                        if (homeState.searchValue == "") "empty" else homeState.searchValue,
                        "home"
                    )
                )
            }
            HomeSideEffect.NavigateToRouteScreen -> {
                // TODO
            }
        }
    }
    
    Column(
        modifier = Modifier
            .background(WanderlustTheme.colors.primaryBackground)
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 64.dp)
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchTextField(
                modifier = Modifier.weight(0.8f),
                searchValue = homeState.searchValue,
                onChange = { searchValue -> eventHandler.invoke(HomeEvent.OnSearchValueChanged(searchValue)) }
            )
            SquareButton(
                modifier = Modifier
                    .weight(0.2f),
                icon = R.drawable.ic_filter,
                iconColor = WanderlustTheme.colors.secondaryText,
                backgroundColor = WanderlustTheme.colors.solid
            ) {
                eventHandler.invoke(HomeEvent.OnFilterBtnClick)
            }
            SquareButton(
                modifier = Modifier.weight(0.2f),
                icon = R.drawable.ic_search,
                iconColor = WanderlustTheme.colors.onAccent,
                backgroundColor = WanderlustTheme.colors.accent
            ) {
                eventHandler.invoke(HomeEvent.OnSearchBtnClick)
            }
        }

        LazyRow(
            modifier = Modifier.padding(top = 22.dp),
            content = {
                item {
                    Text(
                        modifier = Modifier
                            .clickable { eventHandler.invoke(HomeEvent.OnCategoryClick(SortCategory.ALL_ROUTES)) },
                        text = stringResource(id = R.string.all_routes),
                        style = WanderlustTheme.typography.bold20,
                        color = if(homeState.selectedCategory == SortCategory.ALL_ROUTES) WanderlustTheme.colors.primaryText else WanderlustTheme.colors.secondaryText
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .clickable { eventHandler.invoke(HomeEvent.OnCategoryClick(SortCategory.ALL_PLACES)) },
                        text = stringResource(id = R.string.all_places),
                        style = WanderlustTheme.typography.bold20,
                        color = if(homeState.selectedCategory == SortCategory.ALL_PLACES) WanderlustTheme.colors.primaryText else WanderlustTheme.colors.secondaryText
                    )
                }
                item{
                    Text(
                        modifier = Modifier
                            .clickable { eventHandler.invoke(HomeEvent.OnCategoryClick(SortCategory.FAVOURITES)) },
                        text = stringResource(id = R.string.favourites),
                        style = WanderlustTheme.typography.bold20,
                        color = if(homeState.selectedCategory == SortCategory.FAVOURITES) WanderlustTheme.colors.primaryText else WanderlustTheme.colors.secondaryText
                    )
                }
            }
        )
        //ListOfRoutes(routes = )
    }
}
