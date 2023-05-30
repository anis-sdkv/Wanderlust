package com.wanderlust.ui.screens.create_place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val isShowCurrentLocation: Boolean,
)

sealed interface CreatePlaceEvent{
    object OnBackBtnClick: CreatePlaceEvent
    data class OnShowCurrentLocationClicked(val placeLon: Double, val placeLat: Double): CreatePlaceEvent
    data class OnMapClick(val placeLon: Double, val placeLat: Double) : CreatePlaceEvent
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
            isShowCurrentLocation = true,
        )
    )
    val state: StateFlow<CreatePlaceState> = internalState

    private val _action = MutableSharedFlow<CreatePlaceSideEffect?>()
    val action: SharedFlow<CreatePlaceSideEffect?>
        get() = _action.asSharedFlow()

    fun event (createPlaceEvent: CreatePlaceEvent){
        when(createPlaceEvent){
            is CreatePlaceEvent.OnShowCurrentLocationClicked -> onShowCurrentLocationClicked(createPlaceEvent)
            CreatePlaceEvent.OnBackBtnClick -> onBackBtnClick()
            is CreatePlaceEvent.OnMapClick -> onMapClick(createPlaceEvent)
            is CreatePlaceEvent.OnPlaceNameChanged -> onPlaceNameChanged(createPlaceEvent)
            is CreatePlaceEvent.OnPlaceDescriptionChanged -> onPlaceDescriptionChanged(createPlaceEvent)
        }
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
                isShowCurrentLocation = false
            )
        )
    }
}