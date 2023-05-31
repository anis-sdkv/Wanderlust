package com.wanderlust.ui.screens.create_place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.wanderlust.ui.screens.create_route.CreateRouteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreatePlaceState(
    val placeLat: Double,
    val placeLon: Double,
    val placeName: String,
    val placeDescription: String,
    val placeCameraPosition: CameraPosition,
    val isShowCurrentLocation: Boolean,
    val selectedTags: List<String>
)

sealed interface CreatePlaceEvent{
    object OnBackBtnClick: CreatePlaceEvent
    data class OnSelectedTagsChanged(val tag: String) : CreatePlaceEvent
    data class OnShowCurrentLocationClicked(val placeLon: Double, val placeLat: Double): CreatePlaceEvent
    data class OnMapClick(val placeLon: Double, val placeLat: Double, val placeCameraPosition: CameraPosition) : CreatePlaceEvent
    data class OnPlaceNameChanged(val placeName: String) : CreatePlaceEvent
    data class OnPlaceDescriptionChanged(val placeDescription: String) : CreatePlaceEvent
}

sealed interface CreatePlaceSideEffect {
    object NavigateBack: CreatePlaceSideEffect
}

@HiltViewModel
class CreatePlaceViewModel @Inject constructor (

) : ViewModel() {
    private val internalState: MutableStateFlow<CreatePlaceState> = MutableStateFlow(
        CreatePlaceState(
            placeLat = 55.790278,
            placeLon = 49.13472200000001,
            placeName = "",
            placeDescription = "",
            placeCameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(55.790278, 49.13472200000001),
                13f
            ),
            isShowCurrentLocation = true,
            emptyList()
        )
    )
    val state: StateFlow<CreatePlaceState> = internalState

    private val _action = MutableSharedFlow<CreatePlaceSideEffect?>()
    val action: SharedFlow<CreatePlaceSideEffect?>
        get() = _action.asSharedFlow()

    fun event (createPlaceEvent: CreatePlaceEvent){
        when(createPlaceEvent){
            is CreatePlaceEvent.OnSelectedTagsChanged -> onSelectedTagsChanged(createPlaceEvent)
            is CreatePlaceEvent.OnShowCurrentLocationClicked -> onShowCurrentLocationClicked(createPlaceEvent)
            CreatePlaceEvent.OnBackBtnClick -> onBackBtnClick()
            is CreatePlaceEvent.OnMapClick -> onMapClick(createPlaceEvent)
            is CreatePlaceEvent.OnPlaceNameChanged -> onPlaceNameChanged(createPlaceEvent)
            is CreatePlaceEvent.OnPlaceDescriptionChanged -> onPlaceDescriptionChanged(createPlaceEvent)
        }
    }

    private fun onSelectedTagsChanged(event: CreatePlaceEvent.OnSelectedTagsChanged){
        internalState.tryEmit(
            internalState.value.copy(
                selectedTags = internalState.value.selectedTags.toMutableList().also {
                    if(it.contains(event.tag)) it.remove(event.tag) else it.add(event.tag)
                }
            )
        )
    }

    private fun onShowCurrentLocationClicked(event: CreatePlaceEvent.OnShowCurrentLocationClicked){
        internalState.tryEmit(
            internalState.value.copy(
                placeLat = event.placeLat,
                placeLon = event.placeLon,
                isShowCurrentLocation = true
            )
        )
    }

    private fun onPlaceDescriptionChanged(event: CreatePlaceEvent.OnPlaceDescriptionChanged){
        internalState.tryEmit(
            internalState.value.copy(
                placeDescription = event.placeDescription
            )
        )
    }

    private fun onPlaceNameChanged(event: CreatePlaceEvent.OnPlaceNameChanged){
        internalState.tryEmit(
            internalState.value.copy(
                placeName = event.placeName
            )
        )
    }

    private fun onBackBtnClick(){
        viewModelScope.launch {
            _action.emit(CreatePlaceSideEffect.NavigateBack)
        }
    }

    private fun onMapClick(event: CreatePlaceEvent.OnMapClick){
        internalState.tryEmit(
            internalState.value.copy(
                placeLat = event.placeLat,
                placeLon = event.placeLon,
                placeCameraPosition = event.placeCameraPosition,
                isShowCurrentLocation = false
            )
        )
    }
}