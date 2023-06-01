package com.wanderlust.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.SearchTextField
import com.wanderlust.ui.components.common.SquareButton
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.screens.edit_profile.EditProfileEvent

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
                //navController.navigate()
            }
            HomeSideEffect.NavigateToRouteScreen -> {
                //navController.navigate()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(WanderlustTheme.colors.primaryBackground)
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 64.dp),
        //state = lazyListState,
        content = {
            item{
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchTextField(
                        modifier = Modifier.weight(0.8f),
                        searchValue = homeState.searchValue,
                        onChange = { searchValue -> eventHandler.invoke(HomeEvent.OnSearchValueChanged(searchValue)) }
                    )
                    SquareButton(
                        modifier = Modifier.padding(start = 8.dp).weight(0.2f),
                        icon = R.drawable.ic_filter,
                        iconColor = WanderlustTheme.colors.secondaryText,
                        backgroundColor = WanderlustTheme.colors.solid
                    ) {
                        eventHandler.invoke(HomeEvent.OnFilterBtnClick)
                    }
                    SquareButton(
                        modifier = Modifier.padding(start = 8.dp).weight(0.2f),
                        icon = R.drawable.ic_search,
                        iconColor = WanderlustTheme.colors.onAccent,
                        backgroundColor = WanderlustTheme.colors.accent
                    ) {
                        eventHandler.invoke(HomeEvent.OnSearchBtnClick)
                    }
                }
            }
        }
    )
}