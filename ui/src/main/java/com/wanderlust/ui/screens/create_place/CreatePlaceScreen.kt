package com.wanderlust.ui.screens.create_place

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.DefaultDescriptionField
import com.wanderlust.ui.components.common.DefaultTextField
import com.wanderlust.ui.permissions.RequestPermission

import com.wanderlust.ui.theme.WanderlustTextStyles
import com.wanderlust.ui.utils.LocationUtils

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreatePlaceScreen(
    navController: NavController,
    viewModel: CreatePlaceViewModel = hiltViewModel()
) {
    val createPlaceState by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    var requestLocationUpdate by remember { mutableStateOf(true)}

    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)

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
            null -> Unit
            CreatePlaceSideEffect.NavigateBack -> {
                navController.navigateUp()
            }
        }
    }

    if(requestLocationUpdate){
        RequestPermission(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onNavigateBack = { navController.navigateUp() },
            updateCurrentLocation =  {
                requestLocationUpdate = false
                LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->

                    locationResult.lastLocation?.let { location ->

                        currentLocation = location
                    }
                }
            }
        )
    }

    LazyColumn(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ){
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = {
                        eventHandler.invoke(CreatePlaceEvent.OnBackBtnClick)
                    }
                ) {
                    Image(
                        painterResource(R.drawable.ic_back),
                        contentDescription = "icon_back",
                        contentScale = ContentScale.Crop,
                    )
                }

                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(id = R.string.creating_place),
                    style = WanderlustTextStyles.ProfileRoutesTitleText,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.specify_location),
                    style = WanderlustTextStyles.ProfileRouteTitleAndBtnText,
                    color = MaterialTheme.colorScheme.onBackground
                )
                GpsTextButton(
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
            }
        }

        item{
            GoogleMap(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .height(450.dp),
                cameraPositionState = cameraPositionState,
                uiSettings = mapUiSettings,
                onMapClick = {
                    eventHandler.invoke(CreatePlaceEvent.OnMapClick(placeLon = it.longitude, placeLat = it.latitude))
                }
            ) {
                Marker(
                    state = MarkerState(
                        position =
                        if(
                            createPlaceState.isShowCurrentLocation
                        ){
                            LocationUtils.getPosition(currentLocation)
                        } else {
                            LatLng(createPlaceState.placeLat, createPlaceState.placeLon)
                        }
                    ),
                    title = createPlaceState.placeName
                )
            }
        }

        item {
            DefaultTextField(
                label = stringResource(id = R.string.place_name),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 26.dp),
                inputValue = createPlaceState.placeName
            ) { placeName -> eventHandler.invoke(CreatePlaceEvent.OnPlaceNameChanged(placeName)) }

        }

        item {
            DefaultDescriptionField(
                label = stringResource(id = R.string.place_description),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp),
                inputValue = createPlaceState.placeDescription
            ) { placeDescription ->
                eventHandler.invoke(
                    CreatePlaceEvent.OnPlaceDescriptionChanged(
                        placeDescription
                    )
                )
            }
        }

        item {
            LazyRow(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp)
            ){
                items(5){
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

        item {
            Button(
                onClick = {
                    // TODO
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp, start = 24.dp, end = 24.dp, bottom = 80.dp)
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    style = WanderlustTextStyles.ProfileRouteTitleAndBtnText,
                    color = MaterialTheme.colorScheme.background
                )
            }
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
            style = WanderlustTextStyles.ProfileUserInfoText,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
