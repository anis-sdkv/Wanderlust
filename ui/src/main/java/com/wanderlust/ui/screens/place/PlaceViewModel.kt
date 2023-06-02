package com.wanderlust.ui.screens.place

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
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
data class PlaceState(
    val id: String = "",
    val lat: Double = 55.790278,
    val lon: Double = 49.13472200000001,
    val placeName: String = "Place",
    val placeDescription: String = "123",
    val comments: PersistentList<Comment> = persistentListOf(
        Comment(
            "777",
            "Name1",
            1,
            Date(),
            "text1",
        ),
        Comment(
            "0000",
            "Name2",
            1,
            Date(),
            "text2",
        )
    ),
    val createdAt: Date = Date(),
    val totalRating: Int = 25,
    val ratingCount: Int = 6,
    val imagesUrl: PersistentList<String> = persistentListOf(),
    val placeTags: PersistentList<String> = persistentListOf("Day", "Long distance", "In the city", "dsldffmlkefmwkedl"),
    val placeCity: String = "Kazan",
    val placeCountry: String = "Russia",
    val authorName: String = "Author"
)

sealed interface PlaceEvent {
    object OnAuthorClick: PlaceEvent
    object OnBackBtnClick: PlaceEvent
}

sealed interface PlaceSideEffect {
    object NavigateToUserProfileScreen : PlaceSideEffect
    object NavigateBack : PlaceSideEffect
}


@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<PlaceState> = MutableStateFlow(PlaceState())
    val state: StateFlow<PlaceState> = _state

    private val _action = MutableSharedFlow<PlaceSideEffect?>()
    val action: SharedFlow<PlaceSideEffect?>
        get() = _action.asSharedFlow()


    fun event(placeEvent: PlaceEvent){
        when(placeEvent){
            PlaceEvent.OnAuthorClick -> onAuthorClick()
            PlaceEvent.OnBackBtnClick -> onBackBtnClick()
        }
    }

    private fun onBackBtnClick(){
        viewModelScope.launch {
            _action.emit(PlaceSideEffect.NavigateBack)
        }
    }

    private fun onAuthorClick(){
        viewModelScope.launch {
            _action.emit(PlaceSideEffect.NavigateToUserProfileScreen)
        }
    }
}