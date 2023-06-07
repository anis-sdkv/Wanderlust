package com.wanderlust.ui.screens.place

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
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
import com.wanderlust.ui.components.common.AuthorCard
import com.wanderlust.ui.components.common.CommentCard
import com.wanderlust.ui.components.common.CommentEditingHeader
import com.wanderlust.ui.components.common.CommentField
import com.wanderlust.ui.components.common.LocationText
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

    val scrollState = rememberScrollState()

    var imageHeight by remember {
        mutableStateOf(500)
    }
    val imageTranslationY by remember {
        derivedStateOf { scrollState.value * .6f }
    }

    val imageVisibility by remember {
        derivedStateOf { scrollState.value / imageHeight.toFloat() }
    }

    LaunchedEffect(action) {
        when (action) {
            null -> Unit
            PlaceSideEffect.NavigateToUserProfileScreen-> {
                navController.navigate(ProfileNavScreen.Profile.route)
            }
            PlaceSideEffect.NavigateBack -> {
                //navController.navigateUp()
            }
        }
    }

    Column(
        modifier = Modifier
            .background(WanderlustTheme.colors.primaryBackground)
            .fillMaxSize()
            .padding(bottom = 64.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy((-32).dp)
    )
    {
        val gradientColor = WanderlustTheme.colors.secondaryText
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Image(
                painterResource(R.drawable.test_user_photo),
                contentDescription = "user_photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .graphicsLayer {
                        //анимация и изменение непрозрачности изображения при скролле:
                        translationY = imageTranslationY
                        alpha = 1f - imageVisibility
                    }
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, gradientColor),
                            startY = size.height / 3 * 2,
                            endY = size.height
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Multiply)
                        }
                    }
                    .onGloballyPositioned {
                        imageHeight = it.size.height
                    }
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 20.dp, start = 10.dp),
                onClick = {  }
            ) {
                Image(
                    painterResource(R.drawable.ic_back),
                    modifier = Modifier,
                    contentDescription = "icon_back",
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(WanderlustTheme.colors.primaryText)
                )
            }
        }

        Card(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.primaryBackground),
        ) {
            PlaceMainContent(state = state, eventHandler = eventHandler, isDarkMode = isDarkMode)
        }
    }
}

@Composable
fun PlaceMainContent(state: PlaceState, eventHandler: (PlaceEvent) -> Unit, isDarkMode: Boolean?) {
    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LatLng(state.lat, state.lon),
        13f
    )
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
            rating = state.totalRating/state.ratingCount,
            ratingCount = state.ratingCount,
        )

        TagsRow(modifier = Modifier.padding(top = 8.dp), tags = state.placeTags)

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
            userImage = painterResource(id = R.drawable.test_user_photo)) {
            eventHandler.invoke(PlaceEvent.OnAuthorClick)
        }

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
            properties = if(isDarkMode == true) MapProperties(mapStyleOptions = MapStyleOptions(
                DarkMapStyle.json)
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
            text =
            stringResource(id = R.string.added) + " " + state.createdAt.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            style = WanderlustTheme.typography.semibold14,
            color = WanderlustTheme.colors.secondaryText,
            textAlign = TextAlign.End
        )

        // Комментарии
        Text(
            text = stringResource(id = R.string.comments),
            modifier = Modifier.padding(top = 28.dp),
            style = WanderlustTheme.typography.bold20,
            color = WanderlustTheme.colors.primaryText
        )

        if (state.isShowUserComment){
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CommentEditingHeader(
                    modifier = Modifier,
                    onEditBtnClick = { eventHandler.invoke(PlaceEvent.OnEditCommentIconClick) },
                    onDeleteBtnClick = { eventHandler.invoke(PlaceEvent.OnDeleteCommentIconClick) }
                )
                CommentField(
                    modifier = Modifier,
                    comment = state.userComment,
                    images = listOf("url1", "url2", "url3", "url4")
                        //state.userComment.images
                ) {
                    eventHandler.invoke(PlaceEvent.OnAuthorClick)
                }
                Divider(
                    modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp),
                    thickness = 1.dp,
                    color = WanderlustTheme.colors.secondaryText
                )
            }
        } else {

            if(state.isEditComment){
                CommentCard(
                    modifier = Modifier.padding(top = 16.dp),
                    comment = state.userComment,
                    isEditComment = true,
                    onTextChange = { inputCommentText -> eventHandler.invoke(PlaceEvent.OnInputCommentTextChange(inputCommentText)) },
                    onBtnClick = { eventHandler.invoke(PlaceEvent.OnEditComment) },
                    onStarClick = {
                            starNumber -> eventHandler.invoke(PlaceEvent.OnUserPlaceRatingChange(starNumber))
                    }
                )
            } else {
                CommentCard(
                    modifier = Modifier.padding(top = 16.dp),
                    comment = state.userComment,
                    isEditComment = false,
                    onTextChange = { inputCommentText -> eventHandler.invoke(PlaceEvent.OnInputCommentTextChange(inputCommentText)) },
                    onBtnClick = { eventHandler.invoke(PlaceEvent.OnCreateComment) },
                    onStarClick = {
                            starNumber -> eventHandler.invoke(PlaceEvent.OnUserPlaceRatingChange(starNumber))
                    }
                )
            }
        }

        state.comments.forEach { comment ->
            // отображаем все комментрии, кроме комментрия пользователя
            // комментрий пользователя показывается выше
            if(comment.authorNickname != state.userComment.authorNickname) {
                CommentField(
                    modifier = Modifier.padding(top = 16.dp),
                    comment = comment,
                    images = listOf("url1", "url2", "url3", "url4")
                    //state.userComment.images
                ){
                    eventHandler.invoke(PlaceEvent.OnAuthorClick)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
