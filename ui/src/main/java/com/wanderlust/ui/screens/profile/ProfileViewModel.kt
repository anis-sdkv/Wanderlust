package com.wanderlust.ui.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.usecases.GetCurrentUserUseCase
import com.wanderlust.domain.usecases.GetRoutesByIdListUseCase
import com.wanderlust.ui.navigation.graphs.bottom_navigation.ProfileNavScreen
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
data class ProfileState(
    val isUserAuthorized: Boolean = false,
    val isSelfProfile: Boolean = false,
    val isSubscribe: Boolean = false,
    val userName: String = "",
    val userCity: String? = null,
    val userCountry: String? = null,
    val userDescription: String = "",
    val userRoutes: PersistentList<Route> = persistentListOf(),
    val userNumberOfSubscribers: Int = 0,
    val userNumberOfSubscriptions: Int = 0,
    val userNumberOfRoutes: Int = 0,
    val isDropdownMenuExpanded: Boolean = false
)

sealed interface ProfileSideEffect {
    object NavigateToEditProfileScreen : ProfileSideEffect
}

sealed interface ProfileEvent {
    object OnSubscribeBtnClick : ProfileEvent
    object OnDropdownMenuClick : ProfileEvent
    object OnCloseDropdownMenu : ProfileEvent
    object OnEditProfileBtnClick : ProfileEvent
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getRoutesByIdListUseCase: GetRoutesByIdListUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _action = MutableSharedFlow<ProfileSideEffect?>()
    val action: SharedFlow<ProfileSideEffect?>
        get() = _action.asSharedFlow()

    init {
        val id = savedStateHandle.get<String>(ProfileNavScreen.USER_ID_KEY) ?: ProfileNavScreen.SELF_PROFILE
        if (id == ProfileNavScreen.SELF_PROFILE) {
            _state.tryEmit(_state.value.copy(isSelfProfile = true))
            loadSelfProfile()
        } else {
            loadProfileById(id)
        }
    }

    fun event(profileEvent: ProfileEvent) {
        when (profileEvent) {
            ProfileEvent.OnSubscribeBtnClick -> onSubscribeBtnClick()
            ProfileEvent.OnDropdownMenuClick -> onDropdownMenuClick()
            ProfileEvent.OnCloseDropdownMenu -> onCloseDropdownMenu()
            ProfileEvent.OnEditProfileBtnClick -> onEditProfileBtnClick()
        }
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

    private fun loadSelfProfile() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            if (user != null) {
                _state.emit(_state.value.copy(isUserAuthorized = true))
                _state.emit(
                    _state.value.copy(
                        userName = user.username,
                        userCity = user.city,
                        userCountry = user.country,
                        userDescription = user.description ?: "",
                        userNumberOfRoutes = user.routes.size,
                        userNumberOfSubscribers = user.subscribers.size,
                        userNumberOfSubscriptions = user.subscriptions.size
                    )
                )
                try {
                    val routes = getRoutesByIdListUseCase(user.routes)
                    _state.emit(_state.value.copy(userRoutes = routes.toPersistentList()))
                } catch (e: Exception) {
                    e
                }
            }
        }
    }

    private fun loadProfileById(id: String) {
        viewModelScope.launch {

        }
    }
}