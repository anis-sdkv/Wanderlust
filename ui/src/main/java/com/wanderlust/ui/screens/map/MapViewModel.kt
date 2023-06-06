package com.wanderlust.ui.screens.map

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.wanderlust.ui.screens.search.TagsList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@Immutable
data class MapState(
    val lon: Double = 49.13472200000001,
    val lat: Double = 55.790278,
    val searchValue: String = "",
    val selectedTags: PersistentList<String> = persistentListOf(),
    val searchByName: Boolean = true
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
    private val searchByName: Boolean = savedStateHandle["searchType"] ?: true
    private val selectedTags: TagsList = Gson().fromJson(
        Uri.decode(savedStateHandle["searchTags"]), TagsList::class.java
    ) ?: TagsList(emptyList())

    private val internalState: MutableStateFlow<MapState> = MutableStateFlow(
        MapState(
            searchValue = if (searchValue == "empty") "" else searchValue,
            selectedTags = selectedTags.tags.toPersistentList(),
            searchByName = searchByName
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