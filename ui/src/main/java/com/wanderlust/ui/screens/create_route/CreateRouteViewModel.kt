package com.wanderlust.ui.screens.create_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.wanderlust.domain.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateRouteState(
    val placeLat: Double,
    val placeLon: Double,
    val routeName: String,
    val routeDescription: String,
    val isShowCurrentLocation: Boolean,
    val listOfPlaces: List<Place>,
    val itemIdsList: List<Int>,
    val placeCameraPosition: CameraPosition,
    val selectedTags: List<String>
)

sealed interface CreateRouteEvent{
    object OnBackBtnClick: CreateRouteEvent
    data class OnSelectedTagsChanged(val tag: String) : CreateRouteEvent
    data class OnDeletePlaceClick(val place: Place) : CreateRouteEvent
    data class OnAddPlaceClick(val place: Place) : CreateRouteEvent
    data class OnShowCurrentLocationClicked(val placeLon: Double, val placeLat: Double, val place: Place, val placeCameraPosition: CameraPosition): CreateRouteEvent
    data class OnItemClicked(val itemId: Int): CreateRouteEvent
    data class OnMapClick(val placeLon: Double, val placeLat: Double, val place: Place, val placeCameraPosition: CameraPosition) : CreateRouteEvent
    data class OnRouteNameChanged(val routeName: String) : CreateRouteEvent
    data class OnRouteDescriptionChanged(val routeDescription: String) : CreateRouteEvent
    data class OnPlaceNameChanged(val placeName: String, val place: Place) : CreateRouteEvent
    data class OnPlaceDescriptionChanged(val placeDescription: String, val place: Place) : CreateRouteEvent
}

sealed interface CreateRouteSideEffect {
    object NavigateBack: CreateRouteSideEffect
}

@HiltViewModel
class CreateRouteViewModel @Inject constructor (

) : ViewModel() {
    private val internalState: MutableStateFlow<CreateRouteState> = MutableStateFlow(
        CreateRouteState(
            placeLat = 55.790278,
            placeLon = 49.13472200000001,
            routeName = "",
            routeDescription = "",
            isShowCurrentLocation = true,
            emptyList(),
            emptyList(),
            placeCameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(55.790278, 49.13472200000001),
                13f
            ),
            emptyList()
        )
    )
    val state: StateFlow<CreateRouteState> = internalState

    private val _action = MutableSharedFlow<CreateRouteSideEffect?>()
    val action: SharedFlow<CreateRouteSideEffect?>
        get() = _action.asSharedFlow()

    fun event (createRouteEvent: CreateRouteEvent){
        when(createRouteEvent){
            CreateRouteEvent.OnBackBtnClick -> onBackBtnClick()
            is CreateRouteEvent.OnSelectedTagsChanged -> onSelectedTagsChanged(createRouteEvent)
            is CreateRouteEvent.OnDeletePlaceClick -> onDeletePlaceClick(createRouteEvent)
            is CreateRouteEvent.OnAddPlaceClick -> onAddPlaceClick(createRouteEvent)
            is CreateRouteEvent.OnShowCurrentLocationClicked -> onShowCurrentLocationClicked(createRouteEvent)
            is CreateRouteEvent.OnItemClicked -> onItemClicked(createRouteEvent)
            is CreateRouteEvent.OnMapClick -> onMapClick(createRouteEvent)
            is CreateRouteEvent.OnRouteNameChanged -> onRouteNameChanged(createRouteEvent)
            is CreateRouteEvent.OnRouteDescriptionChanged -> onRouteDescriptionChanged(createRouteEvent)
            is CreateRouteEvent.OnPlaceNameChanged -> onPlaceNameChanged(createRouteEvent)
            is CreateRouteEvent.OnPlaceDescriptionChanged -> onPlaceDescriptionChanged(createRouteEvent)
        }
    }

    private fun onSelectedTagsChanged(event: CreateRouteEvent.OnSelectedTagsChanged){
        internalState.tryEmit(
            internalState.value.copy(
                selectedTags = internalState.value.selectedTags.toMutableList().also {
                    if(it.contains(event.tag)) it.remove(event.tag) else it.add(event.tag)
                }
            )
        )
    }


    private fun onPlaceNameChanged(event: CreateRouteEvent.OnPlaceNameChanged){
        internalState.tryEmit(
            internalState.value.copy(
                listOfPlaces = internalState.value.listOfPlaces.toMutableList().also {
                    if (it.contains(event.place)){
                        it[it.indexOf(event.place)] = event.place.copy(
                            placeName = event.placeName
                        )
                    }
                }
            )
        )
    }

    private fun onPlaceDescriptionChanged(event: CreateRouteEvent.OnPlaceDescriptionChanged){
        internalState.tryEmit(
            internalState.value.copy(
                listOfPlaces = internalState.value.listOfPlaces.toMutableList().also {
                    if (it.contains(event.place)){
                        it[it.indexOf(event.place)] = event.place.copy(
                            placeDescription = event.placeDescription
                        )
                    }
                }
            )
        )
    }

    private fun onDeletePlaceClick(event: CreateRouteEvent.OnDeletePlaceClick){
        internalState.tryEmit(
            internalState.value.copy(
                listOfPlaces = internalState.value.listOfPlaces.toMutableList().also {
                    it.remove(event.place)
                },
                itemIdsList = internalState.value.itemIdsList.toMutableList().also {

                }
            )
        )
    }

    private fun onAddPlaceClick(event: CreateRouteEvent.OnAddPlaceClick){
        internalState.tryEmit(
            internalState.value.copy(
                listOfPlaces = internalState.value.listOfPlaces.toMutableList().also {
                    it.add(event.place)
                }
            )
        )
    }

    private fun onItemClicked(event: CreateRouteEvent.OnItemClicked){
        internalState.tryEmit(
            internalState.value.copy(
                itemIdsList = internalState.value.itemIdsList.toMutableList().also {
                    if(it.contains(event.itemId)){
                        it.remove(event.itemId)
                    } else {
                        it.clear()
                        it.add(event.itemId)
                    }
                }
            )
        )
    }

    private fun onShowCurrentLocationClicked(event: CreateRouteEvent.OnShowCurrentLocationClicked){
        internalState.tryEmit(
            internalState.value.copy(
                listOfPlaces = internalState.value.listOfPlaces.toMutableList().also {
                    if (it.contains(event.place)){
                        it[it.indexOf(event.place)] = event.place.copy(
                            lat = event.placeLat,
                            lon = event.placeLon,
                        )
                    }
                },
                isShowCurrentLocation = true,
                placeCameraPosition = event.placeCameraPosition
            )
        )
    }

    private fun onRouteDescriptionChanged(event: CreateRouteEvent.OnRouteDescriptionChanged){
        internalState.tryEmit(
            internalState.value.copy(
                routeDescription = event.routeDescription
            )
        )
    }

    private fun onRouteNameChanged(event: CreateRouteEvent.OnRouteNameChanged){
        internalState.tryEmit(
            internalState.value.copy(
                routeName = event.routeName
            )
        )
    }

    private fun onBackBtnClick(){
        viewModelScope.launch {
            _action.emit(CreateRouteSideEffect.NavigateBack)
        }
    }

    private fun onMapClick(event: CreateRouteEvent.OnMapClick){
        internalState.tryEmit(
            internalState.value.copy(
                listOfPlaces = internalState.value.listOfPlaces.toMutableList().also {
                    if (it.contains(event.place)){
                        it[it.indexOf(event.place)] = event.place.copy(
                            lat = event.placeLat,
                            lon = event.placeLon
                        )
                    }
                },
                isShowCurrentLocation = false,
                placeCameraPosition = event.placeCameraPosition
            )
        )
    }
}