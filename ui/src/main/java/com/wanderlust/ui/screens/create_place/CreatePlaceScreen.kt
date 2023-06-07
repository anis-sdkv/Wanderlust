package com.wanderlust.ui.screens.create_place

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.DefaultButton
import com.wanderlust.ui.components.common.DefaultDescriptionField
import com.wanderlust.ui.components.common.DefaultTextField
import com.wanderlust.ui.components.common.ErrorDialog
import com.wanderlust.ui.components.common.ImagesRow
import com.wanderlust.ui.components.common.LoadingDialog
import com.wanderlust.ui.components.common.ScreenHeader
import com.wanderlust.ui.components.common.TagsField
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.permissions.RequestPermission
import com.wanderlust.ui.screens.edit_profile.EditProfileEvent
import com.wanderlust.ui.screens.map.DarkMapStyle
import com.wanderlust.ui.settings.LocalSettingsEventBus
import com.wanderlust.ui.utils.LocationUtils

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreatePlaceScreen(
    navController: NavController,
    viewModel: CreatePlaceViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    val settingsEventBus = LocalSettingsEventBus.current
    val currentSettings = settingsEventBus.currentSettings.collectAsState().value
    val isDarkMode = currentSettings.isDarkMode

    var requestLocationUpdate by remember { mutableStateOf(true)}

    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)

    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = if (state.isShowCurrentLocation) {
        CameraPosition.fromLatLngZoom(
            LocationUtils.getPosition(currentLocation),
            13f
        )
    } else {
        state.placeCameraPosition
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(zoomControlsEnabled = false)
        )
    }
    LaunchedEffect(Unit) {
        eventHandler.invoke(CreatePlaceEvent.OnLaunch)
    }

    LaunchedEffect(action) {
        when (action) {
            CreatePlaceSideEffect.NavigateBack -> {
                navController.navigateUp()
            }

            CreatePlaceSideEffect.NavigateProfile -> {
                navController.navigate(BottomNavigationItem.Profile.graph)
            }

            else -> Unit
        }
    }

    Dialogs(state = state, eventHandler = eventHandler)

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

    Column(
        Modifier
            .background(WanderlustTheme.colors.primaryBackground)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp, start = 20.dp, end = 20.dp),
    ){
        ScreenHeader(screenName = stringResource(id = R.string.creating_place)) {
            eventHandler.invoke(CreatePlaceEvent.OnBackBtnClick)
        }
        GpsTextButton (
            modifier = Modifier.padding(top = 40.dp),
            onTextClick = {
                eventHandler.invoke(
                    CreatePlaceEvent.OnShowCurrentLocationClicked(
                        LocationUtils.getPosition(currentLocation).latitude,
                        LocationUtils.getPosition(currentLocation).longitude
                    )
                )
                requestLocationUpdate = true
            }
        )


        GoogleMap(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .height(450.dp),
            cameraPositionState = cameraPositionState,
            properties =
                if(isDarkMode == true)
                    MapProperties(mapStyleOptions = MapStyleOptions(DarkMapStyle.json)) else MapProperties(),
            uiSettings = mapUiSettings,
            onMapClick = {
                eventHandler.invoke(
                    CreatePlaceEvent.OnMapClick(
                        placeLon = it.longitude,
                        placeLat = it.latitude,
                        placeCameraPosition = cameraPositionState.position
                    )
                )
            }
        ) {
            Marker(
                state = MarkerState(
                    position =
                    if(
                        state.isShowCurrentLocation
                    ){
                        LocationUtils.getPosition(currentLocation)
                    } else {
                        LatLng(state.placeLat, state.placeLon)
                    }
                ),
                title = state.placeName
            )
        }

        DefaultTextField(
            label = stringResource(id = R.string.place_name),
            modifier = Modifier.padding(top = 26.dp),
            inputValue = state.placeName
        ) { placeName -> eventHandler.invoke(CreatePlaceEvent.OnPlaceNameChanged(placeName)) }

        DefaultDescriptionField(
            label = stringResource(id = R.string.place_description),
            modifier = Modifier.padding(top = 20.dp),
            inputValue = state.placeDescription
        ) { placeDescription ->
            eventHandler.invoke(
                CreatePlaceEvent.OnPlaceDescriptionChanged(
                    placeDescription
                )
            )
        }

        TagsField(
            modifier = Modifier,
            onTagClick = { tag -> eventHandler.invoke(CreatePlaceEvent.OnSelectedTagsChanged(tag)) },
            selectedTags = state.selectedTags
        )

        val images = listOf("url1", "url2", "url3", "url4")
        ImagesRow(
            modifier = Modifier.padding(top = 16.dp),
            gradientColor = WanderlustTheme.colors.primaryBackground,
            isAddingEnable = true,
            imagesUrl = images
        ) {  }

        DefaultButton(
            modifier = Modifier.padding(top = 22.dp, bottom = 80.dp),
            text = stringResource(id = R.string.save),
            buttonColor = WanderlustTheme.colors.accent,
            textColor = WanderlustTheme.colors.onAccent
        ) {
            eventHandler.invoke(CreatePlaceEvent.OnCreteButtonClick)
        }
    }
}

@Composable
private fun GpsTextButton(modifier: Modifier, onTextClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.specify_location),
            style = WanderlustTheme.typography.semibold16,
            color = WanderlustTheme.colors.primaryText
        )
        TextButton(
            modifier = Modifier,
            contentPadding = PaddingValues(
                start = 4.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 4.dp,
            ),
            onClick = {
                onTextClick()
            }
        ) {
            Text(
                text = stringResource(id = R.string.use_your_location),
                style = WanderlustTheme.typography.medium16,
                color = WanderlustTheme.colors.accent,
            )
        }
    }
}


@Composable
private fun Dialogs(state: CreatePlaceState, eventHandler: (CreatePlaceEvent) -> Unit) {

    if (state.showLoadingProgressBar)
        LoadingDialog(
            stringResource(id = R.string.loading_create),
            onDismiss = { eventHandler.invoke(CreatePlaceEvent.OnDismissProgressbarDialog) }
        )

    if (state.showErrorDialog)
        ErrorDialog(
            title = stringResource(id = R.string.error),
            errors = state.errors,
            onDismiss = { eventHandler.invoke(CreatePlaceEvent.OnDismissErrorsDialog) }
        )
}
