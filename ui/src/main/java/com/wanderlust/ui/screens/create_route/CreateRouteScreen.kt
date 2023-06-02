package com.wanderlust.ui.screens.create_route

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.wanderlust.domain.model.RoutePoint
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.DefaultDescriptionField
import com.wanderlust.ui.components.common.DefaultTextField
import com.wanderlust.ui.components.common.ErrorDialog
import com.wanderlust.ui.components.common.LoadingDialog
import com.wanderlust.ui.components.common.ScreenHeader
import com.wanderlust.ui.components.common.TagsField
import com.wanderlust.ui.components.edit_profile_screen.EditProfileTextField
import com.wanderlust.ui.components.edit_profile_screen.EditProfileTextFieldDescription
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.permissions.RequestPermission
import com.wanderlust.ui.utils.LocationUtils

@OptIn(ExperimentalLayoutApi::class, ExperimentalPermissionsApi::class)
@Composable
fun CreateRouteScreen(
    navController: NavController,
    viewModel: CreateRouteViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)
    var requestLocationUpdate by remember { mutableStateOf(true) }

    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)

    LaunchedEffect(Unit) {
        eventHandler.invoke(CreateRouteEvent.OnLaunch)
    }

    if (requestLocationUpdate) {
        RequestPermission(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onNavigateBack = { navController.navigateUp() },
            updateCurrentLocation = {
                requestLocationUpdate = false
                LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->
                    locationResult.lastLocation?.let { location ->
                        currentLocation = location
                    }
                }
            }
        )
    }

    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LocationUtils.getPosition(currentLocation),
        13f
    )
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(zoomControlsEnabled = false)
        )
    }

    LaunchedEffect(action) {
        when (action) {
            CreateRouteSideEffect.NavigateBack -> {
                navController.navigateUp()
            }

            CreateRouteSideEffect.NavigateProfile -> {
                navController.navigate(BottomNavigationItem.Profile.graph)
            }

            else -> Unit
        }
    }

    Dialogs(state = state, eventHandler = eventHandler)

    // content
    Column(
        Modifier
            .background(WanderlustTheme.colors.primaryBackground)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp, start = 20.dp, end = 20.dp),
    ) {

        ScreenHeader(screenName = stringResource(id = R.string.creating_route)) {
            eventHandler.invoke(CreateRouteEvent.OnBackBtnClick)
        }

        GoogleMap(
            modifier = Modifier
                .padding(top = 26.dp, bottom = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .height(450.dp),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
        ) {
            state.listOfPoints.forEach { place ->
                Marker(
                    state = MarkerState(
                        position = LatLng(place.lat, place.lon)
                    ),
                    title = place.name
                )
            }
            val latLonList: MutableList<LatLng> = mutableListOf()
            state.listOfPoints.forEach { place ->
                latLonList.add(LatLng(place.lat, place.lon))
            }
            Polyline(
                points = latLonList,
                color = WanderlustTheme.colors.accent
            )
        }

        DefaultTextField(
            label = stringResource(id = R.string.route_name),
            state.routeName,
            Modifier.padding(top = 26.dp)
        ) { routeName -> eventHandler.invoke(CreateRouteEvent.OnRouteNameChanged(routeName)) }

        DefaultDescriptionField(
            label = stringResource(id = R.string.route_description),
            state.routeDescription,
            Modifier.padding(top = 16.dp)
        ) { routeDescription -> eventHandler.invoke(CreateRouteEvent.OnRouteDescriptionChanged(routeDescription)) }

        TagsField(
            modifier = Modifier,
            onTagClick = { tag -> eventHandler.invoke(CreateRouteEvent.OnSelectedTagsChanged(tag))},
            selectedTags = state.selectedTags
        )

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.route),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, bottom = 20.dp),
            style = WanderlustTheme.typography.bold20,
            color = WanderlustTheme.colors.primaryText
        )

        if (state.listOfPoints.isEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.6f),
                text = stringResource(id = R.string.start_creating),
                textAlign = TextAlign.Center,
                style = WanderlustTheme.typography.semibold14,
                color = WanderlustTheme.colors.primaryText
            )
        }

        state.listOfPoints.forEachIndexed { index, routePoint ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (index != 0) {
                    Icon(
                        painterResource(R.drawable.ic_arrow_down),
                        contentDescription = "icon_trash",
                        modifier = Modifier
                            .size(48.dp),
                        WanderlustTheme.colors.accent
                    )
                }
                Card(
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(10.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column {
                        HeaderView(
                            headerText = "${index + 1}    ${routePoint.name}",
                            onClickItem = {
                                eventHandler.invoke(CreateRouteEvent.OnItemClicked(index))
                            },
                            onDeleteItem = {
                                eventHandler.invoke(CreateRouteEvent.OnDeletePlaceClick(routePoint))
                            },
                            isExpanded = state.expandedItemIndexes.contains(index),
                        )
                        ExpandableView(
                            createRouteState = state,
                            navController,
                            routePoint = routePoint,
                            isExpanded = state.expandedItemIndexes.contains(index),
                            onMapClick = { lat, lon, placeCameraPosition ->
                                eventHandler.invoke(
                                    CreateRouteEvent.OnMapClick(
                                        pointLon = lon,
                                        pointLat = lat,
                                        point = routePoint,
                                        pointCameraPosition = placeCameraPosition
                                    )
                                )
                            },
                            onGpsBtnClick = { lat, lon, cameraPosition ->
                                eventHandler.invoke(
                                    CreateRouteEvent.OnShowCurrentLocationClicked(
                                        pointLat = lat,
                                        pointLon = lon,
                                        point = routePoint,
                                        pointCameraPosition = cameraPosition
                                    )
                                )
                            },
                            index = index,
                            fusedLocationProviderClient = fusedLocationProviderClient,
                            onPlaceNameChanged = { placeName ->
                                eventHandler.invoke(CreateRouteEvent.OnPlaceNameChanged(placeName, routePoint))
                            },
                            onPlaceDescriptionChanged = { placeDescription ->
                                eventHandler.invoke(
                                    CreateRouteEvent.OnPlaceDescriptionChanged(
                                        placeDescription,
                                        routePoint
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    eventHandler.invoke(
                        CreateRouteEvent.OnAddPlaceClick(
                            RoutePoint(
                                currentLocation.latitude,
                                currentLocation.longitude,
                                "Interesting Place",
                                ""
                            )
                        )
                    )
                },
                modifier = Modifier
                    .padding(top = 22.dp, bottom = 16.dp)
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WanderlustTheme.colors.accent),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "icon_add",
                    modifier = Modifier,
                    WanderlustTheme.colors.onAccent
                )
                Text(
                    text = stringResource(id = R.string.add_place),
                    style = WanderlustTheme.typography.semibold16,
                    color = WanderlustTheme.colors.onAccent
                )
            }

        }

        Button(
            onClick = { eventHandler.invoke(CreateRouteEvent.OnCreteButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp, bottom = 80.dp)
                .height(42.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WanderlustTheme.colors.accent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.save),
                style = WanderlustTheme.typography.semibold16,
                color = WanderlustTheme.colors.primaryBackground
            )
        }
    }
}

@Composable
private fun GpsTextButton(onTextClick: () -> Unit) {
    TextButton(
        modifier = Modifier,
        onClick = {
            onTextClick()
        }
    ) {
        Text(
            text = stringResource(id = R.string.use_your_location),
            style = WanderlustTheme.typography.semibold14,
            color = WanderlustTheme.colors.accent,
        )
    }
}

@Composable
fun HeaderView(
    headerText: String,
    onClickItem: () -> Unit,
    onDeleteItem: () -> Unit,
    isExpanded: Boolean
) {
    Card(
        border = BorderStroke(1.dp, WanderlustTheme.colors.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null, // Removes the ripple effect on tap
                interactionSource = remember { MutableInteractionSource() }, // Removes the ripple effect on tap
                onClick = onClickItem
            ),
        shape = if(isExpanded) RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp) else RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.solid)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = headerText,
                style = WanderlustTheme.typography.semibold16,
                color = WanderlustTheme.colors.primaryText,
                modifier = Modifier
                //.fillMaxWidth()
            )

            IconButton(
                modifier = Modifier,
                onClick = {
                    onDeleteItem()
                }
            ) {
                Icon(
                    painterResource(R.drawable.ic_trash),
                    contentDescription = "icon_trash",
                    modifier = Modifier
                        .size(30.dp),
                    WanderlustTheme.colors.accent
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExpandableView(
    createRouteState: CreateRouteState,
    navController: NavController,
    routePoint: RoutePoint,
    isExpanded: Boolean,
    index: Int,
    onMapClick: (Double, Double, CameraPosition) -> Unit,
    onGpsBtnClick: (Double, Double, CameraPosition) -> Unit,
    onPlaceNameChanged: (String) -> Unit,
    onPlaceDescriptionChanged: (String) -> Unit,
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }

    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position =
        if(createRouteState.isShowCurrentLocation){
            CameraPosition.fromLatLngZoom(
                LocationUtils.getPosition(currentLocation),
                13f
            )
        } else {
            createRouteState.placeCameraPosition
        }

    var requestPlaceLocationUpdate by remember { mutableStateOf(true)}

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(zoomControlsEnabled = false)
        )
    }

    if (requestPlaceLocationUpdate){
        RequestPermission(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onNavigateBack = { navController.navigateUp() },
            updateCurrentLocation =  {
                requestPlaceLocationUpdate = false
                LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->

                    locationResult.lastLocation?.let { location ->

                        currentLocation = location
                    }
                }
            }
        )
    }

    // Opening Animation
    val expandTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(
            animationSpec = tween(300)
        )
    }

    // Closing Animation
    val collapseTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        Column(
            modifier = Modifier
                .background(WanderlustTheme.colors.solid)
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = stringResource(id = R.string.specify_location),
                    style = WanderlustTheme.typography.semibold14,
                    color = WanderlustTheme.colors.primaryText
                )
                GpsTextButton(
                    onTextClick = {
                        onGpsBtnClick(
                            LocationUtils.getPosition(currentLocation).latitude,
                            LocationUtils.getPosition(currentLocation).longitude,
                            CameraPosition.fromLatLngZoom(
                                LocationUtils.getPosition(currentLocation),
                                cameraPositionState.position.zoom
                            ),
                        )
                        requestPlaceLocationUpdate = true
                    }
                )
            }
            GoogleMap(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .height(270.dp),
                cameraPositionState = cameraPositionState,
                uiSettings = mapUiSettings,
                onMapClick = {
                    onMapClick(it.latitude, it.longitude, cameraPositionState.position)
                }
            ) {
                Marker(
                    state = MarkerState(
                        position =
                        LatLng(createRouteState.listOfPoints[index].lat, createRouteState.listOfPoints[index].lon)
                    ),
                    title = createRouteState.listOfPoints[index].name
                )
            }
            EditProfileTextField(
                label = stringResource(id = R.string.place_name),
                inputValue = createRouteState.listOfPoints[index].name,
                //modifier = Modifier.padding(top = 28.dp),
            ){ placeName -> onPlaceNameChanged(placeName) }

            EditProfileTextFieldDescription(
                label = stringResource(id = R.string.place_description),
                inputValue = createRouteState.listOfPoints[index].description ?: "",
                //modifier = Modifier.padding(top = 16.dp),
            ){ placeDescription -> onPlaceDescriptionChanged(placeDescription) }

            LazyRow(
                modifier = Modifier.padding(top = 16.dp)
            ){
                items(3){
                    Image(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp)
                            .size(120.dp)
                            .clip(shape = RoundedCornerShape(16.dp)),
                        painter = painterResource(id = R.drawable.test_user_photo),
                        contentDescription = "Place Image"
                    )
                }
            }
        }
    }
}

@Composable
private fun Dialogs(state: CreateRouteState, eventHandler: (CreateRouteEvent) -> Unit) {

    if (state.showLoadingProgressBar)
        LoadingDialog(
            stringResource(id = R.string.loading_create),
            onDismiss = { eventHandler.invoke(CreateRouteEvent.OnDismissProgressbarDialog) }
        )

    if (state.showErrorDialog)
        ErrorDialog(
            title = stringResource(id = R.string.error),
            errors = state.errors,
            onDismiss = { eventHandler.invoke(CreateRouteEvent.OnDismissErrorsDialog) }
        )
}
