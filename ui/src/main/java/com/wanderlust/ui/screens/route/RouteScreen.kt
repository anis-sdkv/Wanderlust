package com.wanderlust.ui.screens.route

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.wanderlust.domain.model.RoutePoint
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.AnimatedBackgroundImage
import com.wanderlust.ui.components.common.AuthorCard
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
import com.wanderlust.ui.settings.LocalSettingsEventBus
import kotlin.math.roundToInt

@Composable
fun RouteScreen(
    navController: NavController,
    viewModel: RouteViewModel = hiltViewModel()
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
            is RouteSideEffect.NavigateToUserProfileScreen -> {
                navController.navigate(ProfileNavScreen.Profile.passUserId((action as RouteSideEffect.NavigateToUserProfileScreen).id))
            }

            RouteSideEffect.NavigateBack -> {
                //navController.navigateUp()
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
            RouteCardState.PROGRESS_BAR -> item { LoadingCard() }
            RouteCardState.NOT_FOUND -> item {
                MessageCard(
                    title = stringResource(id = R.string.empty),
                    message = stringResource(id = R.string.not_found)
                )
            }

            RouteCardState.CONTENT -> {

                item {
                    RouteMainContent(
                        state = state,
                        eventHandler = eventHandler,
                        isDarkMode = isDarkMode
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                items(state.comments.size) {
                    if (state.comments[it].authorNickname != state.currentUser?.username && state.comments[it].text?.isNotEmpty() == true) {
                        CommentField(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                            comment = state.comments[it],
                            images = listOf()
                        ) {
                            eventHandler.invoke(RouteEvent.OnAuthorClick)
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
fun RouteMainContent(state: RouteState, eventHandler: (RouteEvent) -> Unit, isDarkMode: Boolean?) {
    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LatLng(state.points[0].lat, state.points[0].lon),
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
            // Имя пользователя
            Text(
                text = state.routeName,
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
                userRouteRating = state.userRouteRating,
                onStarClick = { starNumber ->
                    eventHandler.invoke(RouteEvent.OnUserRouteRatingChange(starNumber))
                }
            )

            TagsRow(modifier = Modifier.padding(top = 16.dp), tags = state.routeTags)

            LocationText(
                city = state.routeCity, country = state.routeCountry,
                Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp),
                arrangement = Arrangement.Start
            )

            // Описание
            if (state.routeDescription.isNotEmpty())
                RouteDescriptionField(
                    modifier = Modifier.padding(top = 28.dp),
                    routeDescription = state.routeDescription
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
                eventHandler.invoke(RouteEvent.OnAuthorClick)
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
                properties = if (isDarkMode == true) MapProperties(
                    mapStyleOptions = MapStyleOptions(
                        DarkMapStyle.json
                    )
                ) else MapProperties()
            ) {
                state.points.forEach { point ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(point.lat, point.lon)
                        ),
                        title = point.name
                    )
                }
                val latLonList: MutableList<LatLng> = mutableListOf()
                state.points.forEach { point ->
                    latLonList.add(LatLng(point.lat, point.lon))
                }
                Polyline(
                    points = latLonList,
                    color = WanderlustTheme.colors.accent
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

            // Точки маршрута
            Text(
                text = stringResource(id = R.string.points_of_route),
                modifier = Modifier.padding(top = 28.dp),
                style = WanderlustTheme.typography.bold20,
                color = WanderlustTheme.colors.primaryText
            )
            Spacer(modifier = Modifier.height(12.dp))
            state.points.forEach { point ->
                RoutePoint(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp), place = point)
            }

            CommentTextField(
                isUserAuth = state.currentUser != null,
                comment = if (state.inEditMode) null else state.comments.find { it.authorId == state.currentUser?.id },
                inputValue = state.inputCommentText,
                onChange = { inputCommentText ->
                    eventHandler.invoke(RouteEvent.OnInputCommentTextChange(inputCommentText))
                },
                ratingValue = state.userRouteRating ?: 0,
                onRatingChange = { i -> eventHandler.invoke(RouteEvent.OnUserRouteRatingChange(i)) },
                onSendBtnClick = { eventHandler.invoke(RouteEvent.OnCreateComment) },
                modifier = Modifier.padding(top = 16.dp),
                onDeleteCommentClick = { eventHandler.invoke(RouteEvent.OnDeleteCommentIconClick) },
                onEditCommentClick = { eventHandler.invoke(RouteEvent.OnEditComment) },
            )

            // Комментарии
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
fun RouteDescriptionField(modifier: Modifier, routeDescription: String){
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.secondaryBackground),
    ){
        Text(
            text = routeDescription,
            modifier = Modifier.padding(16.dp),
            style = WanderlustTheme.typography.medium16,
            color = WanderlustTheme.colors.primaryText
        )
    }
}

// Это пунктирная линия,
// но пока не работает так, как надо, потом разберусь:
private data class DottedShape(
    val step: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(Path().apply {
        val stepPx = with(density) { step.toPx() }
        val stepsCount = (size.height / stepPx).roundToInt()
        val actualStep = size.height / stepsCount
        val dotSize = Size(height = actualStep / 2, width = size.width)
        for (i in 0 until stepsCount) {
            addRect(
                Rect(
                    offset = Offset(y = i * actualStep, x = 0f),
                    size = dotSize
                )
            )
        }
        close()
    })
}

@Composable
fun RoutePoint(modifier: Modifier, place: RoutePoint){

    ConstraintLayout(
        modifier = modifier//.clipToBounds()
    ) {
        val (icon, line, card) = createRefs()
        Icon(
            painterResource(R.drawable.ic_point),
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(top = 20.dp)
                .size(20.dp),
            contentDescription = "icon_point",
            tint = WanderlustTheme.colors.accent
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            Modifier
                .constrainAs(line) {
                    top.linkTo(icon.bottom)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(top = 16.dp)
                .fillMaxHeight()
                .width(4.dp)
                .background(
                    color = WanderlustTheme.colors.primaryText,
                    shape = DottedShape(step = 10.dp)
                )
        )
        PlaceCard(
            modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(icon.end)
                }
                .padding(start = 20.dp),
            place = place
        )
    }
}

@Composable
fun PlaceCard(modifier: Modifier, place: RoutePoint){
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.secondaryBackground),
    ){
        Text(
            text = place.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            style = WanderlustTheme.typography.bold16,
            color = WanderlustTheme.colors.primaryText,
            textAlign = TextAlign.Start
        )

        if (place.description != null){
            Text(
                text = place.description ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                style = WanderlustTheme.typography.medium12,
                color = WanderlustTheme.colors.primaryText,
                textAlign = TextAlign.Start
            )
        }
        
        if (true
            //place.imagesUrl != null
        ){
            Text(
                text = stringResource(id = R.string.photos),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp),
                style = WanderlustTheme.typography.semibold14,
                color = WanderlustTheme.colors.primaryText,
                textAlign = TextAlign.Start
            )
            LazyRow(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)
            ){
                items(5){
                    Image(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(64.dp)
                            .clip(shape = RoundedCornerShape(16.dp)),
                        painter = painterResource(id = R.drawable.test_user_photo),
                        contentDescription = "Place Image"
                    )
                }
            }
        }
        Text(
            text = place.lat.toString() + ", " + place.lon.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp, end = 16.dp),
            style = WanderlustTheme.typography.medium12,
            color = WanderlustTheme.colors.secondaryText,
            textAlign = TextAlign.End,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
private fun Dialogs(state: RouteState, eventHandler: (RouteEvent) -> Unit) {

    if (state.showLoadingDialog)
        LoadingDialog(
            stringResource(id = R.string.loading_create),
            onDismiss = { eventHandler.invoke(RouteEvent.OnDismissProgressbarDialog) }
        )

    if (state.showErrorsDialog)
        ErrorDialog(
            title = stringResource(id = R.string.error),
            errors = state.errors,
            onDismiss = { eventHandler.invoke(RouteEvent.OnDismissErrorsDialog) }
        )
}
