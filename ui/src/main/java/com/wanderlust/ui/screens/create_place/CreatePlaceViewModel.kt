package com.wanderlust.ui.screens.create_place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.Place
import com.wanderlust.domain.usecases.CreatePlaceUseCase
import com.wanderlust.domain.usecases.GetCurrentUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@Immutable
data class CreatePlaceState(
    val placeLat: Double = 55.790278,
    val placeLon: Double = 49.13472200000001,
    val placeName: String = "",
    val placeDescription: String = "",
    val placeCameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(
        LatLng(55.790278, 49.13472200000001),
        13f
    ),
    val isShowCurrentLocation: Boolean = true,
    val selectedTags: PersistentList<String> = persistentListOf(),
    val showLoadingProgressBar: Boolean = false,
    val showErrorDialog: Boolean = false,
    val errors: PersistentList<String> = persistentListOf(),
)

sealed interface CreatePlaceEvent {
    object OnBackBtnClick : CreatePlaceEvent
    data class OnSelectedTagsChanged(val tag: String) : CreatePlaceEvent
    data class OnShowCurrentLocationClicked(val placeLon: Double, val placeLat: Double) : CreatePlaceEvent
    data class OnMapClick(val placeLon: Double, val placeLat: Double, val placeCameraPosition: CameraPosition) :
        CreatePlaceEvent

    data class OnPlaceNameChanged(val placeName: String) : CreatePlaceEvent
    data class OnPlaceDescriptionChanged(val placeDescription: String) : CreatePlaceEvent
    object OnDismissErrorsDialog : CreatePlaceEvent
    object OnDismissProgressbarDialog : CreatePlaceEvent
    object OnCreteButtonClick : CreatePlaceEvent
    object OnLaunch : CreatePlaceEvent
}

sealed interface CreatePlaceSideEffect {
    object NavigateBack : CreatePlaceSideEffect
    object NavigateProfile : CreatePlaceSideEffect
}

@HiltViewModel
class CreatePlaceViewModel @Inject constructor(
    private val createPlaceUseCase: CreatePlaceUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<CreatePlaceState> = MutableStateFlow(CreatePlaceState())
    val state: StateFlow<CreatePlaceState> = _state

    private val _action = MutableSharedFlow<CreatePlaceSideEffect?>(replay = 1)
    val action: SharedFlow<CreatePlaceSideEffect?>
        get() = _action.asSharedFlow()

    private var currentJob: Job? = null
    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
        currentJob = null
    }

    fun event(event: CreatePlaceEvent) {
        when (event) {
            CreatePlaceEvent.OnBackBtnClick -> onBackBtnClick()
            is CreatePlaceEvent.OnMapClick -> onMapClick(event.placeLat, event.placeLon, event.placeCameraPosition)
            is CreatePlaceEvent.OnPlaceNameChanged -> onPlaceNameChanged(event.placeName)
            is CreatePlaceEvent.OnPlaceDescriptionChanged -> onPlaceDescriptionChanged(event.placeDescription)
            is CreatePlaceEvent.OnSelectedTagsChanged -> onSelectedTagsChanged(event.tag)
            is CreatePlaceEvent.OnShowCurrentLocationClicked -> onShowCurrentLocationClicked(
                event.placeLat,
                event.placeLon
            )

            CreatePlaceEvent.OnDismissErrorsDialog -> dismissErrorsDialog()
            CreatePlaceEvent.OnDismissProgressbarDialog -> dismissProgressbarDialog()
            CreatePlaceEvent.OnCreteButtonClick -> createPlace()
            CreatePlaceEvent.OnLaunch -> onLaunch()
        }
    }

    private fun onLaunch() {
        val id = getCurrentUserIdUseCase()
        if (id == null)
            viewModelScope.launch {
                _action.emit(CreatePlaceSideEffect.NavigateProfile)
            }
    }

    private fun onSelectedTagsChanged(tag: String) {
        val list = _state.value.selectedTags.toMutableList().also {
            if (it.contains(tag)) it.remove(tag) else it.add(tag)
        }.toPersistentList()
        _state.tryEmit(_state.value.copy(selectedTags = list))
    }

    private fun dismissProgressbarDialog() {
        currentJob?.cancel()
        _state.tryEmit(_state.value.copy(showLoadingProgressBar = false))
    }

    private fun dismissErrorsDialog() {
        _state.tryEmit(_state.value.copy(showErrorDialog = false))
    }

    private fun onShowCurrentLocationClicked(lat: Double, lon: Double) {
        _state.tryEmit(
            _state.value.copy(
                placeLat = lat,
                placeLon = lon,
                isShowCurrentLocation = true
            )
        )
    }

    private fun onPlaceDescriptionChanged(description: String) {
        _state.tryEmit(_state.value.copy(placeDescription = description))
    }

    private fun onPlaceNameChanged(name: String) {
        _state.tryEmit(_state.value.copy(placeName = name))
    }

    private fun onBackBtnClick() {
        viewModelScope.launch {
            _action.emit(CreatePlaceSideEffect.NavigateBack)
        }
    }

    private fun onMapClick(lat: Double, lon: Double, cameraPosition: CameraPosition) {
        _state.tryEmit(
            _state.value.copy(
                placeLat = lat,
                placeLon = lon,
                placeCameraPosition = cameraPosition,
                isShowCurrentLocation = false
            )
        )
    }

    private fun createPlace() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val errors = mutableListOf<String>()
            val place = Place(
                lat = state.value.placeLat,
                lon = state.value.placeLon,
                placeDescription = state.value.placeDescription,
                placeName = state.value.placeName,
                createdAt = Date(),
                tags = state.value.selectedTags,
                authorId = null,
                authorName = null
            )
            _state.emit(_state.value.copy(showLoadingProgressBar = true))
            val result = createPlaceUseCase(place)
            _state.emit(_state.value.copy(showLoadingProgressBar = false))

            when (result) {
                is FirestoreActionResult.SuccessResult -> {
                    _action.emit(CreatePlaceSideEffect.NavigateBack)
                }

                is FirestoreActionResult.FailResult -> {
                    result.message?.let { errors.add(it) }
                    _state.emit(_state.value.copy(showErrorDialog = true, errors = errors.toPersistentList()))
                }
            }
        }
    }
}