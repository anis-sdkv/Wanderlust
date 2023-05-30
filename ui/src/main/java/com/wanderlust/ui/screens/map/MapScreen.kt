package com.wanderlust.ui.screens.map

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.wanderlust.ui.R
import com.wanderlust.ui.navigation.graphs.Graph
import com.wanderlust.ui.permissions.RequestPermission
import com.wanderlust.ui.utils.LocationUtils

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel()
) {

    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }

    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LocationUtils.getPosition(currentLocation),
        13f
    )
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)

    var requestLocationUpdate by remember { mutableStateOf(true)}

    if(requestLocationUpdate){
        RequestPermission(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onNavigateBack = { navController.navigate(Graph.BOTTOM) },
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

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        Marker(
            state = MarkerState(position = LocationUtils.getPosition(currentLocation)),
            title = stringResource(id = R.string.it_is_you)
        )
    }
}
