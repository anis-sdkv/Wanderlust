package com.wanderlust.ui.screens.map

import androidx.lifecycle.SavedStateHandle
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


data class MapState(
    val lon: Double,
    val lat: Double,
    val searchValue: String,
    val selectedTags: List<String>,
    val typeOfSearch: Boolean
)

sealed interface MapEvent{
    object OnFilterBtnClick: MapEvent
    object OnSearchBtnClick: MapEvent
    data class OnSearchValueChanged(val searchValue: String): MapEvent
}

sealed interface MapSideEffect {
    object NavigateToSearchScreen: MapSideEffect
}

@HiltViewModel
class MapViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val searchValue: String = savedStateHandle["searchValue"] ?: ""
    private val searchType: Boolean = savedStateHandle["searchType"] ?: true
    //private val selectedTags: TagsList = savedStateHandle["searchTags"] ?: TagsList(emptyList())

    private val internalState: MutableStateFlow<MapState> = MutableStateFlow(
        MapState(
            lon = 49.13472200000001,
            lat = 55.790278,
            if (searchValue == "empty") "" else searchValue,
            emptyList(), // selectedTags.tags
            searchType
        )
    )
    val state: StateFlow<MapState> = internalState

    private val _action = MutableSharedFlow<MapSideEffect?>()
    val action: SharedFlow<MapSideEffect?>
        get() = _action.asSharedFlow()

    fun event(mapEvent: MapEvent){
        when(mapEvent){
            MapEvent.OnFilterBtnClick -> onFilterBtnClick()
            MapEvent.OnSearchBtnClick -> onSearchBtnClick()
            is MapEvent.OnSearchValueChanged -> onSearchValueChanged(mapEvent)
        }
    }

    private fun onSearchValueChanged(event: MapEvent.OnSearchValueChanged){
        internalState.tryEmit(
            internalState.value.copy(
                searchValue = event.searchValue
            )
        )
    }

    private fun onSearchBtnClick(){
        viewModelScope.launch {
            // TODO
        }
    }

    private fun onFilterBtnClick(){
        viewModelScope.launch {
            _action.emit(MapSideEffect.NavigateToSearchScreen)
        }
    }

}