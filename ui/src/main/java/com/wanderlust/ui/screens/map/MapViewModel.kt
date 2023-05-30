package com.wanderlust.ui.screens.map

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


data class MapState(
    val lon: Double,
    val lat: Double
)

sealed interface MapEvent{

}

@HiltViewModel
class MapViewModel @Inject constructor (
) : ViewModel() {
    private val internalState: MutableStateFlow<MapState> = MutableStateFlow(
        MapState(
            lon = 49.13472200000001,
            lat = 55.790278
        )
    )
    val state: StateFlow<MapState> = internalState

}