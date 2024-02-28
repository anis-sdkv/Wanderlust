package com.wanderlust.ui.screens.create_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.model.RoutePoint
import com.wanderlust.domain.usecases.CreateRouteUseCase
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
data class CreateRouteState(
    val placeLat: Double = 55.790278,
    val placeLon: Double = 49.13472200000001,
    val routeName: String = "",
    val routeDescription: String = "",
    val isShowCurrentLocation: Boolean = true,
    val listOfPoints: PersistentList<RoutePoint> = persistentListOf(),
    val expandedItemIndexes: PersistentList<Int> = persistentListOf(),
    val placeCameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(
        LatLng(55.790278, 49.13472200000001),
        13f
    ),
    val selectedTags: PersistentList<String> = persistentListOf(),
    val showLoadingProgressBar: Boolean = false,
    val showErrorDialog: Boolean = false,
    val errors: PersistentList<String> = persistentListOf(),
)

sealed interface CreateRouteEvent {
    object OnBackBtnClick : CreateRouteEvent
    object OnDismissProgressbarDialog : CreateRouteEvent
    object OnDismissErrorsDialog : CreateRouteEvent
    object OnCreteButtonClick : CreateRouteEvent
    object OnLaunch : CreateRouteEvent
    data class OnSelectedTagsChanged(val tag: String) : CreateRouteEvent
    data class OnDeletePlaceClick(val point: RoutePoint) : CreateRouteEvent
    data class OnAddPlaceClick(val point: RoutePoint) : CreateRouteEvent
    data class OnShowCurrentLocationClicked(
        val pointLon: Double,
        val pointLat: Double,
        val point: RoutePoint,
        val pointCameraPosition: CameraPosition
    ) : CreateRouteEvent

    data class OnItemClicked(val itemId: Int) : CreateRouteEvent
    data class OnMapClick(
        val pointLon: Double,
        val pointLat: Double,
        val point: RoutePoint,
        val pointCameraPosition: CameraPosition
    ) : CreateRouteEvent

    data class OnRouteNameChanged(val routeName: String) : CreateRouteEvent
    data class OnRouteDescriptionChanged(val routeDescription: String) : CreateRouteEvent
    data class OnPlaceNameChanged(val placeName: String, val point: RoutePoint) : CreateRouteEvent
    data class OnPlaceDescriptionChanged(val placeDescription: String, val point: RoutePoint) : CreateRouteEvent
}

sealed interface CreateRouteSideEffect {
    object NavigateBack : CreateRouteSideEffect
    object NavigateProfile : CreateRouteSideEffect
}

@HiltViewModel
class CreateRouteViewModel @Inject constructor (
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val createRouteUseCase: CreateRouteUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<CreateRouteState> = MutableStateFlow(
        CreateRouteState()
    )
    val state: StateFlow<CreateRouteState> = _state

    private val _action = MutableSharedFlow<CreateRouteSideEffect?>(replay = 1)
    val action: SharedFlow<CreateRouteSideEffect?>
        get() = _action.asSharedFlow()

    private var currentJob: Job? = null

    fun event(createRouteEvent: CreateRouteEvent) {
        when (createRouteEvent) {
            CreateRouteEvent.OnBackBtnClick -> onBackBtnClick()
            is CreateRouteEvent.OnSelectedTagsChanged -> onSelectedTagsChanged(createRouteEvent.tag)
            is CreateRouteEvent.OnDeletePlaceClick -> onDeletePlaceClick(createRouteEvent)
            is CreateRouteEvent.OnAddPlaceClick -> onAddPlaceClick(createRouteEvent)
            is CreateRouteEvent.OnShowCurrentLocationClicked -> onShowCurrentLocationClicked(createRouteEvent)
            is CreateRouteEvent.OnItemClicked -> onItemClicked(createRouteEvent)
            is CreateRouteEvent.OnMapClick -> onMapClick(createRouteEvent)
            is CreateRouteEvent.OnRouteNameChanged -> onRouteNameChanged(createRouteEvent)
            is CreateRouteEvent.OnRouteDescriptionChanged -> onRouteDescriptionChanged(createRouteEvent)
            is CreateRouteEvent.OnPlaceNameChanged -> onPlaceNameChanged(createRouteEvent)
            is CreateRouteEvent.OnPlaceDescriptionChanged -> onPlaceDescriptionChanged(createRouteEvent)
            CreateRouteEvent.OnDismissErrorsDialog -> dismissErrorsDialog()
            CreateRouteEvent.OnDismissProgressbarDialog -> dismissProgressbarDialog()
            CreateRouteEvent.OnCreteButtonClick -> create()
            CreateRouteEvent.OnLaunch -> onLaunch()
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
        currentJob = null
    }

    private fun create() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val errors = mutableListOf<String>()
            val route = Route(
                createdAt = Date(),
                tags = state.value.selectedTags,
                routeName = state.value.routeName,
                routeDescription = state.value.routeDescription,
                points = state.value.listOfPoints,
                authorId = null,
                authorName = null
            )
            _state.emit(_state.value.copy(showLoadingProgressBar = true))
            val result = createRouteUseCase(route)
            _state.emit(_state.value.copy(showLoadingProgressBar = false))

            when (result) {
                is FirestoreActionResult.SuccessResult -> {
                    _action.emit(CreateRouteSideEffect.NavigateBack)
                }

                is FirestoreActionResult.FailResult -> {
                    result.message?.let { errors.add(it) }
                    _state.emit(_state.value.copy(showErrorDialog = true, errors = errors.toPersistentList()))
                }
            }
        }
    }

    private fun onLaunch() {
        val id = getCurrentUserIdUseCase()
        if (id == null)
            viewModelScope.launch {
                _action.emit(CreateRouteSideEffect.NavigateProfile)
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

    private fun onPlaceNameChanged(event: CreateRouteEvent.OnPlaceNameChanged) {
        val list = _state.value.listOfPoints.toMutableList().also {
            if (it.contains(event.point))
                it[it.indexOf(event.point)] = event.point.copy(name = event.placeName)
        }.toPersistentList()

        _state.tryEmit(_state.value.copy(listOfPoints = list))
    }

    private fun onPlaceDescriptionChanged(event: CreateRouteEvent.OnPlaceDescriptionChanged) {
        val list = _state.value.listOfPoints.toMutableList().also {
            if (it.contains(event.point))
                it[it.indexOf(event.point)] = event.point.copy(description = event.placeDescription)
        }.toPersistentList()

        _state.tryEmit(_state.value.copy(listOfPoints = list))
    }

    private fun onDeletePlaceClick(event: CreateRouteEvent.OnDeletePlaceClick) {
        val listOfPoints = _state.value.listOfPoints.toMutableList().also {
            it.remove(event.point)
        }.toPersistentList()
        _state.tryEmit(_state.value.copy(listOfPoints = listOfPoints))
    }

    private fun onAddPlaceClick(event: CreateRouteEvent.OnAddPlaceClick) {
        val listOfPoints = _state.value.listOfPoints.toMutableList().also {
            it.add(event.point)
        }.toPersistentList()
        _state.tryEmit(_state.value.copy(listOfPoints = listOfPoints))
    }

    private fun onItemClicked(event: CreateRouteEvent.OnItemClicked) {
        val expandedItemIndexes = _state.value.expandedItemIndexes.toMutableList().also {
            if (it.contains(event.itemId)) {
                it.remove(event.itemId)
            } else {
                it.clear()
                it.add(event.itemId)
            }
        }.toPersistentList()
        _state.tryEmit(_state.value.copy(expandedItemIndexes = expandedItemIndexes))
    }

    private fun onShowCurrentLocationClicked(event: CreateRouteEvent.OnShowCurrentLocationClicked) {
        val listOfPoints = _state.value.listOfPoints.toMutableList().also {
            if (it.contains(event.point)) {
                it[it.indexOf(event.point)] = event.point.copy(
                    lat = event.pointLat,
                    lon = event.pointLon,
                )
            }
        }.toPersistentList()

        _state.tryEmit(
            _state.value.copy(
                listOfPoints = listOfPoints,
                isShowCurrentLocation = true,
                placeCameraPosition = event.pointCameraPosition
            )
        )
    }

    private fun onRouteDescriptionChanged(event: CreateRouteEvent.OnRouteDescriptionChanged) {
        _state.tryEmit(_state.value.copy(routeDescription = event.routeDescription))
    }

    private fun onRouteNameChanged(event: CreateRouteEvent.OnRouteNameChanged) {
        _state.tryEmit(_state.value.copy(routeName = event.routeName))
    }

    private fun onBackBtnClick() {
        viewModelScope.launch { _action.emit(CreateRouteSideEffect.NavigateBack) }
    }

    private fun onMapClick(event: CreateRouteEvent.OnMapClick) {
        val listOfPoints = _state.value.listOfPoints.toMutableList().also {
            if (it.contains(event.point)) {
                it[it.indexOf(event.point)] = event.point.copy(
                    lat = event.pointLat,
                    lon = event.pointLon
                )
            }
        }.toPersistentList()
        _state.tryEmit(
            _state.value.copy(
                listOfPoints = listOfPoints,
                isShowCurrentLocation = false,
                placeCameraPosition = event.pointCameraPosition
            )
        )
    }
}