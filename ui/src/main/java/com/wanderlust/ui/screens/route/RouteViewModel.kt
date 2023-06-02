package com.wanderlust.ui.screens.route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.model.RoutePoint
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
data class RouteState(
    val routeName: String = "Route",
    val routeDescription: String = "12345",
    val createdAt: Date = Date(),
    val points: PersistentList<RoutePoint> = persistentListOf(
        RoutePoint(
            lat = 55.790278,
            lon = 49.13472200000001,
            "place1",
            "1234",
            emptyList()
        ),
        RoutePoint(
            lat = 55.780278,
            lon = 49.13072200000001,
            "place2",
            "5678",
            emptyList()
        )
    ),
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
    val totalRating: Int = 25,
    val ratingCount: Int = 6,
    val routeTags: PersistentList<String> = persistentListOf("Day", "Long distance", "In the city", "dsldffmlkefmwkedl"),
    val routeCity: String = "Kazan",
    val routeCountry: String = "Russia",
    val authorName: String = "Author"
)

sealed interface RouteEvent {
    object OnAuthorClick: RouteEvent
    object OnBackBtnClick: RouteEvent
}

sealed interface RouteSideEffect {
    object NavigateToUserProfileScreen : RouteSideEffect
    object NavigateBack : RouteSideEffect
}


@HiltViewModel
class RouteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<RouteState> = MutableStateFlow(RouteState())
    val state: StateFlow<RouteState> = _state

    private val _action = MutableSharedFlow<RouteSideEffect?>()
    val action: SharedFlow<RouteSideEffect?>
        get() = _action.asSharedFlow()


    fun event(routeEvent: RouteEvent){
        when(routeEvent){
            RouteEvent.OnAuthorClick -> onAuthorClick()
            RouteEvent.OnBackBtnClick -> onBackBtnClick()
        }
    }

    private fun onBackBtnClick(){
        viewModelScope.launch {
            _action.emit(RouteSideEffect.NavigateBack)
        }
    }

    private fun onAuthorClick(){
        viewModelScope.launch {
            _action.emit(RouteSideEffect.NavigateToUserProfileScreen)
        }
    }
}