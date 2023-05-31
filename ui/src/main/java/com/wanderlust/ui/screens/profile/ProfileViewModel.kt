package com.wanderlust.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.usecases.GetUserByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val isUserAuthorized: Boolean = false,
    val isMyProfile: Boolean = false,
    val isSubscribe: Boolean = false,
    //val subscribeBtnSelected: Boolean
    val userName: String = "",
    val userCity: String = "",
    val userCountry: String = "",
    val userDescription: String = "",
    val userRoutes: List<Route> = emptyList(),
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
//    savedStateHandle: SavedStateHandle,
    private val getUserByName: GetUserByName,
) : ViewModel() {

    //    private val userNameOfProfile: String = savedStateHandle["userName"]!!
    private val userNameOfProfile: String = "Ivan"
    private val user = getUserByName(userNameOfProfile)

    private val isSubscribe = (user.userSubscriptions.find { it.userName == userNameOfProfile }) != null

    private val internalState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = internalState

    private val _action = MutableSharedFlow<ProfileSideEffect?>()
    val action: SharedFlow<ProfileSideEffect?>
        get() = _action.asSharedFlow()

    init {
        setData()
    }

    fun setData(){
        internalState.tryEmit(
            internalState.value.copy(
                isUserAuthorized = true,
                isMyProfile = true, //(userNameOfProfile == user.userName),
                isSubscribe = false,
                //isSubscribe,
                userName = user.userName,
                userCity = user.userCity,
                userCountry = user.userCountry,
                userDescription = user.userDescription,
                userRoutes = user.userRoutes,
                userNumberOfSubscribers = user.userSubscribers.size,
                userNumberOfSubscriptions = user.userSubscriptions.size,
                userNumberOfRoutes = user.userSubscriptions.size,
                isDropdownMenuExpanded = false
            )
        )
    }

    fun event (profileEvent: ProfileEvent){
        when(profileEvent){
            ProfileEvent.OnSubscribeBtnClick -> onSubscribeBtnClick()
            ProfileEvent.OnDropdownMenuClick -> onDropdownMenuClick()
            ProfileEvent.OnCloseDropdownMenu -> onCloseDropdownMenu()
            ProfileEvent.OnEditProfileBtnClick -> onEditProfileBtnClick()
        }
    }

    private fun onSubscribeBtnClick() {
        internalState.tryEmit(
            internalState.value.copy(
                isSubscribe = !internalState.value.isSubscribe
            )
        )
    }

    private fun onEditProfileBtnClick() {
        viewModelScope.launch {
            _action.emit(ProfileSideEffect.NavigateToEditProfileScreen)
        }
    }

    private fun onDropdownMenuClick() {
        internalState.tryEmit(
            internalState.value.copy(
                isDropdownMenuExpanded = true
            )
        )
        /*viewModelScope.launch {
            internalState.emit(
                internalState.value.copy(
                    isDropdownMenuExpanded = true
                )
            )
        }*/
    }

    private fun onCloseDropdownMenu() {
        internalState.tryEmit(
            internalState.value.copy(
                isDropdownMenuExpanded = false
            )
        )
        /*viewModelScope.launch {
            internalState.emit(
                internalState.value.copy(
                    isDropdownMenuExpanded = false
                )
            )
        }*/
    }

}