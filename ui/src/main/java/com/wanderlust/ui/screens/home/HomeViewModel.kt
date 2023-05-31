package com.wanderlust.ui.screens.home

import androidx.lifecycle.ViewModel
import com.wanderlust.domain.model.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject


data class HomeState(
    val routes: List<Route>
)

sealed interface HomeEvent{}

sealed interface HomeSideEffect {
}

@HiltViewModel
class HomeViewModel @Inject constructor (
    //savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val internalState: MutableStateFlow<HomeState> = MutableStateFlow(
        HomeState(
            emptyList()
        )
    )
    val state: StateFlow<HomeState> = internalState

    private val _action = MutableSharedFlow<HomeSideEffect?>()
    val action: SharedFlow<HomeSideEffect?>
        get() = _action.asSharedFlow()
}