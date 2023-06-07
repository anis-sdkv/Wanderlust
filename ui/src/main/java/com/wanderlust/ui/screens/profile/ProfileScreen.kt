package com.wanderlust.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.AnimatedBackgroundImage
import com.wanderlust.ui.components.common.LoadingCard
import com.wanderlust.ui.components.common.LocationText
import com.wanderlust.ui.components.common.MapEntityCard
import com.wanderlust.ui.components.common.SwitchButton
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.navigation.graphs.Graph
import com.wanderlust.ui.navigation.graphs.bottom_navigation.ProfileNavScreen
import com.wanderlust.ui.utils.calculateRating

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(Unit) {
        eventHandler.invoke(ProfileEvent.OnLaunch)
    }
    DisposableEffect(Unit) {
        onDispose {
            eventHandler.invoke(ProfileEvent.OnDispose)
        }
    }

    LaunchedEffect(action) {
        when (action) {
            null -> Unit
            ProfileSideEffect.NavigateToEditProfileScreen -> {
                navController.navigate(ProfileNavScreen.EditProfile.route)
            }

            ProfileSideEffect.NavigateToLoginScreen -> {
                navController.navigate(Graph.AUTHENTICATION)
            }
        }
    }

    val columnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .background(WanderlustTheme.colors.primaryBackground)
            .fillMaxSize()
            .padding(bottom = 64.dp),
        verticalArrangement = Arrangement.spacedBy((-32).dp),
        state = columnState
    )
    {
        item {
            AnimatedBackgroundImage(columnState = columnState)
        }
        when (state.cardState) {
            ProfileCardState.ERROR -> item { ProfileError() }
            ProfileCardState.PROGRESS_BAR -> item { LoadingCard() }
            ProfileCardState.CONTENT -> {
                item {
                    ProfileMainContent(state, eventHandler)
                }
                items(state.userRoutes.size) {
                    MapEntityCard(
                        name = state.userRoutes[it].routeName,
                        rating = state.userRoutes[it].calculateRating(),
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp),
                        onClick = { throw Exception() }
                    )
                }
                if (state.userRoutes.isNotEmpty())
//                    item {
//                        Box(
//                            modifier = Modifier
//                                .padding(bottom = 84.dp, top = 40.dp)
//                                .fillMaxWidth(), contentAlignment = Alignment.Center
//                        ) {
//                            TextButton(
//                                onClick = {},
//                            ) {
//                                Text(
//                                    text = stringResource(id = R.string.show_all_routes),
//                                    style = WanderlustTheme.typography.medium16,
//                                    color = WanderlustTheme.colors.accent,
//                                )
//                            }
//                        }
//                    }
                else
                    item {
                        Text(
                            text = stringResource(id = R.string.not_yet),
                            style = WanderlustTheme.typography.medium16,
                            color = WanderlustTheme.colors.primaryText
                        )
                    }
            }

            ProfileCardState.NOT_AUTH -> item { UserNotAuthMessage(eventHandler) }
        }

    }
}

@Composable
private fun ProfileError() {
    Card(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.primaryBackground),
    ) {
        Text(
            text = stringResource(id = R.string.error),
            modifier = Modifier
                .padding(top = 32.dp),
            style = WanderlustTheme.typography.bold24,
            color = WanderlustTheme.colors.primaryText
        )

        Text(
            text = stringResource(id = R.string.try_again),
            modifier = Modifier.padding(top = 32.dp),
            style = WanderlustTheme.typography.semibold14,
            color = WanderlustTheme.colors.primaryText
        )
    }
}

@Composable
fun ProfileMainContent(state: ProfileState, eventHandler: (ProfileEvent) -> Unit) {
    Column(
        Modifier
            .background(
                WanderlustTheme.colors.primaryBackground,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            )
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            // Имя пользователя
            Text(
                text = state.userName,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
                style = WanderlustTheme.typography.bold24,
                color = WanderlustTheme.colors.primaryText
            )
            // Кнопка DropdownMenu
            IconButton(
                onClick = { eventHandler.invoke(ProfileEvent.OnDropdownMenuClick) },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painterResource(R.drawable.ic_dropdown_menu),
                    modifier = Modifier,
                    contentDescription = "icon_dropdown_menu",
                    tint = WanderlustTheme.colors.secondaryText
                )
            }
        }

        ProfileDropDownMenu(
            state = state,
            eventHandler = eventHandler,
            modifier = Modifier
                .align(Alignment.End)
                .offset(x = (-120).dp)
        )

        LocationText(
            city = state.userCity, country = state.userCountry,
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            arrangement = Arrangement.Center
        )

        ProfileStatistic(state = state, eventHandler = eventHandler, modifier = Modifier.padding(top = 20.dp))

        // Кнопка подписаться / редактировать профиль
        if (state.isSelfProfile)
            SwitchButton(
                onClick = { eventHandler.invoke(ProfileEvent.OnEditProfileBtnClick) },
                pressed = false,
                text = stringResource(id = R.string.edit),
                modifier = Modifier.padding(top = 20.dp)
            )
        else
            SwitchButton(
                onClick = { eventHandler.invoke(ProfileEvent.OnSubscribeBtnClick) },
                pressed = state.isSubscribe,
                text = if (state.isSubscribe) stringResource(id = R.string.unsubscribe) else stringResource(id = R.string.subscribe),
                modifier = Modifier.padding(top = 20.dp)
            )

        // Описание
        if (state.userDescription.isNotEmpty())
            Text(
                text = state.userDescription,
                color = WanderlustTheme.colors.primaryText,
                modifier = Modifier.padding(top = 20.dp),
                style = WanderlustTheme.typography.medium16,
            )

        // Популярное у автора
        Text(
            text = stringResource(id = R.string.popular_from_author),
            modifier = Modifier.padding(vertical = 36.dp),
            style = WanderlustTheme.typography.bold20,
            color = WanderlustTheme.colors.primaryText
        )
    }
}

@Composable
fun UserNotAuthMessage(eventHandler: (ProfileEvent) -> Unit) {
    Card(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.primaryBackground),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.empty),
                modifier = Modifier
                    .padding(top = 32.dp),
                style = WanderlustTheme.typography.bold24,
                color = WanderlustTheme.colors.primaryText
            )

            Text(
                text = stringResource(id = R.string.empty_profile_description),
                modifier = Modifier.padding(top = 32.dp),
                style = WanderlustTheme.typography.semibold14,
                color = WanderlustTheme.colors.primaryText
            )

            Button(
                onClick = { eventHandler.invoke(ProfileEvent.OnButtonLoginClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 28.dp, end = 28.dp)
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(WanderlustTheme.colors.accent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in),
                    style = WanderlustTheme.typography.semibold16,
                    color = WanderlustTheme.colors.onAccent
                )
            }
        }
    }
}


@Composable
private fun ProfileStatistic(
    state: ProfileState,
    eventHandler: (ProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {},
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.userRoutes.size.toString(),
                style = WanderlustTheme.typography.extraBold26,
                color = WanderlustTheme.colors.primaryText
            )
            Text(
                text = stringResource(id = R.string.routes),
                style = WanderlustTheme.typography.medium13,
                color = WanderlustTheme.colors.accent
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    // TODO
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.userNumberOfSubscribers.toString(),
                style = WanderlustTheme.typography.extraBold26,
                color = WanderlustTheme.colors.primaryText
            )
            Text(
                text = stringResource(id = R.string.subscribers),
                style = WanderlustTheme.typography.medium13,
                color = WanderlustTheme.colors.accent
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {},
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.userNumberOfSubscriptions.toString(),
                style = WanderlustTheme.typography.extraBold26,
                color = WanderlustTheme.colors.primaryText
            )
            Text(
                text = stringResource(id = R.string.subscriptions),
                style = WanderlustTheme.typography.medium13,
                color = WanderlustTheme.colors.accent
            )
        }
    }
}

@Composable
private fun ProfileDropDownMenu(
    state: ProfileState,
    eventHandler: (ProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        DropdownMenu(
            modifier = Modifier
                .background(color = WanderlustTheme.colors.secondaryBackground)
                .clip(RoundedCornerShape(8.dp)),
            expanded = state.isDropdownMenuExpanded,
            onDismissRequest = {
                eventHandler.invoke(ProfileEvent.OnCloseDropdownMenu)
            },
        ) {
            ProfileDropDownItem(text = stringResource(id = R.string.sign_out)) { eventHandler.invoke(ProfileEvent.OnSignOutButtonClick) }
        }
    }
}

@Composable
private fun ProfileDropDownItem(text: String, onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                style = WanderlustTheme.typography.semibold14,
                color = WanderlustTheme.colors.secondaryText,
                textAlign = TextAlign.Center
            )
        },
        onClick = onClick
    )
}

