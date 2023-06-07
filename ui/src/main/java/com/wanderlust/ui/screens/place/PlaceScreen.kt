package com.wanderlust.ui.screens.place

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.AnimatedBackgroundImage
import com.wanderlust.ui.components.common.AuthorCard
import com.wanderlust.ui.components.common.CommentEditingHeader
import com.wanderlust.ui.components.common.CommentField
import com.wanderlust.ui.components.common.CommentTextField
import com.wanderlust.ui.components.common.ErrorDialog
import com.wanderlust.ui.components.common.LoadingCard
import com.wanderlust.ui.components.common.LoadingDialog
import com.wanderlust.ui.components.common.LocationText
import com.wanderlust.ui.components.common.MessageCard
import com.wanderlust.ui.components.common.RatingRow
import com.wanderlust.ui.components.common.TagsRow
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.navigation.graphs.bottom_navigation.ProfileNavScreen
import com.wanderlust.ui.screens.map.DarkMapStyle
import com.wanderlust.ui.screens.route.RouteDescriptionField
import com.wanderlust.ui.settings.LocalSettingsEventBus

@Composable
fun PlaceScreen(
    navController: NavController,
    viewModel: PlaceViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    val settingsEventBus = LocalSettingsEventBus.current
    val currentSettings = settingsEventBus.currentSettings.collectAsState().value
    val isDarkMode = currentSettings.isDarkMode


    LaunchedEffect(action) {
        when (action) {
            null -> Unit
            PlaceSideEffect.NavigateToUserProfileScreen -> {
                navController.navigate(ProfileNavScreen.Profile.route)
            }

            PlaceSideEffect.NavigateBack -> {
                navController.navigateUp()
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
        state = columnState,
    )
    {
        item {
            AnimatedBackgroundImage(columnState = columnState)
        }

        when (state.cardState) {
            PlaceCardState.PROGRESS_BAR -> item { LoadingCard() }
            PlaceCardState.NOT_FOUND -> item {
                MessageCard(
                    title = stringResource(id = R.string.empty),
                    message = stringResource(id = R.string.not_found)
                )
            }

            PlaceCardState.CONTENT -> {

                item {
                    PlaceMainContent(
                        state = state,
                        eventHandler = eventHandler,
                        isDarkMode = isDarkMode
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                items(state.comments.size) {
                    if (state.comments[it].authorNickname != state.currentUser?.username) {
                        CommentField(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                            comment = state.comments[it],
                            images = listOf()
                        ) {
                            eventHandler.invoke(PlaceEvent.OnAuthorClick)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    Dialogs(state = state, eventHandler = eventHandler)
}

@Composable
fun PlaceMainContent(state: PlaceState, eventHandler: (PlaceEvent) -> Unit, isDarkMode: Boolean?) {
    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LatLng(state.lat, state.lon),
        13f
    )
    Card(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.primaryBackground),
    ) {

        Column(
            Modifier
                .background(WanderlustTheme.colors.primaryBackground)
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxSize()
        ) {
            // Назавние места
            Text(
                text = state.placeName,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                style = WanderlustTheme.typography.bold24,
                color = WanderlustTheme.colors.primaryText
            )

            RatingRow(
                modifier = Modifier,
                rating = state.rating,
                ratingCount = state.ratingCount,
                userRouteRating = state.userPlaceRating,
                onStarClick = { starNumber ->
                    eventHandler.invoke(PlaceEvent.OnUserPlaceRatingChange(starNumber))
                }
            )

            TagsRow(modifier = Modifier.padding(top = 12.dp), tags = state.placeTags)

            LocationText(
                city = state.placeCity, country = state.placeCountry,
                Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp),
                arrangement = Arrangement.Start
            )

            // Описание
            if (state.placeDescription.isNotEmpty())
                RouteDescriptionField(
                    modifier = Modifier.padding(top = 28.dp),
                    routeDescription = state.placeDescription
                )

            // Автор
            Text(
                text = stringResource(id = R.string.author),
                modifier = Modifier.padding(top = 28.dp),
                style = WanderlustTheme.typography.bold20,
                color = WanderlustTheme.colors.primaryText
            )

            AuthorCard(
                modifier = Modifier.padding(top = 20.dp),
                userName = state.authorName,
                userImage = painterResource(id = R.drawable.test_user_photo)
            ) {
                eventHandler.invoke(PlaceEvent.OnAuthorClick)
            }
//
//            Column(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                CommentEditingHeader(
//                    modifier = Modifier,
//                    onEditBtnClick = { eventHandler.invoke(PlaceEvent.OnEditCommentIconClick) },
//                    onDeleteBtnClick = { eventHandler.invoke(PlaceEvent.OnDeleteCommentIconClick) }
//                )
//                CommentField(
//                    modifier = Modifier,
//                    comment = state.userComment,
//                    images = listOf("url1", "url2", "url3", "url4")
//                    //state.userComment.images
//                ) {
//                    eventHandler.invoke(PlaceEvent.OnAuthorClick)
//                }
//                Divider(
//                    modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp),
//                    thickness = 1.dp,
//                    color = WanderlustTheme.colors.secondaryText
//                )
//            }


            // На карте
            Text(
                text = stringResource(id = R.string.on_map),
                modifier = Modifier
                    .padding(top = 28.dp),
                style = WanderlustTheme.typography.bold20,
                color = WanderlustTheme.colors.primaryText
            )

            GoogleMap(
                modifier = Modifier
                    .padding(top = 26.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .height(450.dp),
                cameraPositionState = cameraPositionState,
                properties = if (isDarkMode == true) MapProperties(
                    mapStyleOptions = MapStyleOptions(
                        DarkMapStyle.json
                    )
                ) else MapProperties()
            ) {
                Marker(
                    state = MarkerState(
                        position = LatLng(state.lat, state.lon)
                    ),
                    title = state.placeName
                )
            }

            Text(
                text = stringResource(id = R.string.added) + " " + state.createdAt.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                style = WanderlustTheme.typography.semibold14,
                color = WanderlustTheme.colors.secondaryText,
                textAlign = TextAlign.End
            )

            // Комментарии
            Text(
                text = stringResource(id = R.string.leave_review),
                modifier = Modifier.padding(top = 28.dp),
                style = WanderlustTheme.typography.bold20,
                color = WanderlustTheme.colors.primaryText
            )

            CommentTextField(
                isUserAuth = state.currentUser != null,
                inputValue = state.inputCommentText,
                onChange = { inputCommentText ->
                    eventHandler.invoke(
                        PlaceEvent.OnInputCommentTextChange(
                            inputCommentText
                        )
                    )
                },
                onSendBtnClick = { eventHandler.invoke(PlaceEvent.OnCreateComment) },
                modifier = Modifier.padding(top = 16.dp),
            )

            Text(
                text = stringResource(id = R.string.comments),
                modifier = Modifier.padding(top = 28.dp),
                style = WanderlustTheme.typography.bold20,
                color = WanderlustTheme.colors.primaryText
            )

            if (state.comments.size == 0)
                Text(
                    text = stringResource(id = R.string.not_yet),
                    modifier = Modifier.padding(top = 16.dp),
                    style = WanderlustTheme.typography.medium16,
                    color = WanderlustTheme.colors.primaryText,
                )
        }
    }
}

@Composable
private fun Dialogs(state: PlaceState, eventHandler: (PlaceEvent) -> Unit) {

    if (state.showLoadingDialog)
        LoadingDialog(
            stringResource(id = R.string.loading_create),
            onDismiss = { eventHandler.invoke(PlaceEvent.OnDismissProgressbarDialog) }
        )

    if (state.showErrorsDialog)
        ErrorDialog(
            title = stringResource(id = R.string.error),
            errors = state.errors,
            onDismiss = { eventHandler.invoke(PlaceEvent.OnDismissErrorsDialog) }
        )
}

