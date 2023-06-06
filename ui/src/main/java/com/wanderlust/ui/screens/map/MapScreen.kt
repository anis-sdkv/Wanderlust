package com.wanderlust.ui.screens.map

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.SearchTextField
import com.wanderlust.ui.components.common.SquareButton
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.navigation.graphs.Graph
import com.wanderlust.ui.navigation.graphs.bottom_navigation.MapNavScreen
import com.wanderlust.ui.permissions.RequestPermission
import com.wanderlust.ui.settings.LocalSettingsEventBus
import com.wanderlust.ui.utils.LocationUtils

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel()
) {
    val mapState by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    val settingsEventBus = LocalSettingsEventBus.current
    val currentSettings = settingsEventBus.currentSettings.collectAsState().value
    val isDarkMode = currentSettings.isDarkMode

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

    LaunchedEffect(action) {
        when (action) {
            null -> Unit
            MapSideEffect.NavigateToSearchScreen -> {
                navController.navigate(
                    MapNavScreen.Search.passSearchValue(
                        if (mapState.searchValue == "") "empty" else mapState.searchValue,
                        "map"
                    )
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(bottom = 64.dp)
            .fillMaxSize()
    ){
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = if(isDarkMode == true) MapProperties(mapStyleOptions = MapStyleOptions(DarkMapStyle.json)) else MapProperties()
        ) {
            Marker(
                state = MarkerState(position = LocationUtils.getPosition(currentLocation)),
                title = stringResource(id = R.string.it_is_you)
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchTextField(
                modifier = Modifier.weight(1f),
                searchValue = mapState.searchValue,
                onChange = { searchValue -> eventHandler.invoke(MapEvent.OnSearchValueChanged(searchValue)) }
            )
            SquareButton(
                icon = R.drawable.ic_filter,
                iconColor = WanderlustTheme.colors.secondaryText,
                backgroundColor = WanderlustTheme.colors.solid,
                onClick = { eventHandler.invoke(MapEvent.OnFilterBtnClick) }
            )
            SquareButton(
                icon = R.drawable.ic_search,
                iconColor = WanderlustTheme.colors.onAccent,
                backgroundColor = WanderlustTheme.colors.accent,
                onClick = { eventHandler.invoke(MapEvent.OnFilterBtnClick) }
            )
        }

        Box(
            Modifier
                .align(alignment = Alignment.TopCenter)
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            WanderlustTheme.colors.primaryBackground.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        ) {

        }
    }
}
