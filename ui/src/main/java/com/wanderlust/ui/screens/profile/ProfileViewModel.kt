package com.wanderlust.ui.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Place
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.usecases.GetCurrentUserUseCase
import com.wanderlust.domain.usecases.GetPlacesByIdListUseCase
import com.wanderlust.domain.usecases.GetRoutesByIdListUseCase
import com.wanderlust.domain.usecases.GetUserUseCase
import com.wanderlust.domain.usecases.SignOutUseCase
import com.wanderlust.ui.navigation.graphs.bottom_navigation.ProfileNavScreen
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
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

enum class ProfileCardState {
    PROGRESS_BAR, CONTENT, NOT_AUTH, ERROR
}

enum class ListState {
    PROGRESS_BAR, CONTENT, ERROR
}

@Immutable
data class ProfileState(
    val isSelfProfile: Boolean = false,
    val isSubscribe: Boolean = false,
    val userName: String = "",
    val userCity: String? = null,
    val userCountry: String? = null,
    val userDescription: String = "",
    val userRoutes: PersistentList<Route> = persistentListOf(),
    val userPlaces: PersistentList<Place> = persistentListOf(),
    val userNumberOfSubscribers: Int = 0,
    val userNumberOfSubscriptions: Int = 0,
    val userNumberOfRoutes: Int = 0,
    val isDropdownMenuExpanded: Boolean = false,
    val routesListState: ListState = ListState.PROGRESS_BAR,
    val placesState: ListState = ListState.PROGRESS_BAR,
    val cardState: ProfileCardState = ProfileCardState.PROGRESS_BAR
)

sealed interface ProfileSideEffect {
    object NavigateToEditProfileScreen : ProfileSideEffect
    object NavigateToLoginScreen : ProfileSideEffect
    data class NavigateToRouteScreen(val id: String) : ProfileSideEffect
    data class NavigateToPlaceScreen(val id: String) : ProfileSideEffect
}

sealed interface ProfileEvent {
    object OnLaunch : ProfileEvent
    object OnDispose : ProfileEvent
    object OnSubscribeBtnClick : ProfileEvent
    object OnDropdownMenuClick : ProfileEvent
    object OnCloseDropdownMenu : ProfileEvent
    object OnEditProfileBtnClick : ProfileEvent
    object OnButtonLoginClick : ProfileEvent
    object OnSignOutButtonClick : ProfileEvent
    data class OnRouteClick(val id: String) : ProfileEvent
    data class OnPlaceClick(val id: String) : ProfileEvent
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getRoutesByIdListUseCase: GetRoutesByIdListUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val signOut: SignOutUseCase,
    private val getPlacesByIdListUseCase: GetPlacesByIdListUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _action = MutableSharedFlow<ProfileSideEffect?>()
    val action: SharedFlow<ProfileSideEffect?>
        get() = _action.asSharedFlow()

    private var currentJob: Job? = null

    fun event(profileEvent: ProfileEvent) {
        when (profileEvent) {
            ProfileEvent.OnLaunch -> initState()
            ProfileEvent.OnDispose -> onDispose()
            ProfileEvent.OnSubscribeBtnClick -> onSubscribeBtnClick()
            ProfileEvent.OnDropdownMenuClick -> onDropdownMenuClick()
            ProfileEvent.OnCloseDropdownMenu -> onCloseDropdownMenu()
            ProfileEvent.OnEditProfileBtnClick -> onEditProfileBtnClick()
            ProfileEvent.OnButtonLoginClick -> onButtonLoginClick()
            ProfileEvent.OnSignOutButtonClick -> onSignOutButtonClick()
            is ProfileEvent.OnPlaceClick -> onPlaceClick(profileEvent.id)
            is ProfileEvent.OnRouteClick -> onRouteClick(profileEvent.id)
        }
    }

    private fun initState() {
        val id = savedStateHandle[ProfileNavScreen.USER_ID_KEY] ?: ProfileNavScreen.SELF_PROFILE
        loadProfile(id)
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
        currentJob = null
    }

    private fun onDispose() {
        _state.tryEmit(ProfileState())
        currentJob?.cancel()
    }

    private fun onRouteClick(id: String) {
        viewModelScope.launch { _action.emit(ProfileSideEffect.NavigateToRouteScreen(id)) }
    }

    private fun onPlaceClick(id: String) {
        viewModelScope.launch { _action.emit(ProfileSideEffect.NavigateToPlaceScreen(id)) }
    }

    private fun onSubscribeBtnClick() {
        _state.tryEmit(
            _state.value.copy(
                isSubscribe = !_state.value.isSubscribe
            )
        )
    }

    private fun onEditProfileBtnClick() {
        viewModelScope.launch {
            _action.emit(ProfileSideEffect.NavigateToEditProfileScreen)
        }
    }

    private fun onDropdownMenuClick() {
        _state.tryEmit(
            _state.value.copy(
                isDropdownMenuExpanded = true
            )
        )
    }

    private fun onCloseDropdownMenu() {
        _state.tryEmit(
            _state.value.copy(
                isDropdownMenuExpanded = false
            )
        )
    }

    private fun onButtonLoginClick() {
        viewModelScope.launch {
            _action.emit(ProfileSideEffect.NavigateToLoginScreen)
        }
    }

    private fun onSignOutButtonClick() {
        viewModelScope.launch {
            signOut()
            _state.emit(_state.value.copy(cardState = ProfileCardState.NOT_AUTH))
            _action.emit(ProfileSideEffect.NavigateToLoginScreen)
        }
    }

    private fun loadProfile(id: String) {
        currentJob = viewModelScope.launch {
            try {
                val user = if (id == ProfileNavScreen.SELF_PROFILE) {
                    _state.tryEmit(_state.value.copy(isSelfProfile = true))
                    getCurrentUserUseCase()
                } else
                    getUserUseCase(id)

                if (user != null) {
                    _state.emit(
                        _state.value.copy(
                            userName = user.username,
                            userCity = user.city,
                            userCountry = user.country,
                            userDescription = user.description ?: "",
                            userNumberOfRoutes = user.routes.size,
                            userNumberOfSubscribers = user.subscribers.size,
                            userNumberOfSubscriptions = user.subscriptions.size,
                            cardState = ProfileCardState.CONTENT,
                        )
                    )
                    launch {
                        loadRoutes(user.routes)
                        loadPlaces(user.places)
                    }
                } else {
                    _state.emit(_state.value.copy(cardState = if (state.value.isSelfProfile) ProfileCardState.NOT_AUTH else ProfileCardState.ERROR))
                }
            } catch (_: Exception) {
                _state.emit(_state.value.copy(cardState = ProfileCardState.ERROR))
            }
        }
    }

    private suspend fun loadRoutes(routesId: List<String>) {
        try {
            val routes = getRoutesByIdListUseCase(routesId)
            _state.emit(_state.value.copy(userRoutes = routes.toPersistentList(), routesListState = ListState.CONTENT))
        } catch (e: Exception) {
            _state.emit(_state.value.copy(routesListState = ListState.ERROR))
        }
    }

    private suspend fun loadPlaces(placesId: List<String>) {
        try {
            val places = getPlacesByIdListUseCase(placesId)
            _state.emit(_state.value.copy(userPlaces = places.toPersistentList(), placesState = ListState.CONTENT))
        } catch (e: Exception) {
            _state.emit(_state.value.copy(placesState = ListState.ERROR))
        }
    }
}