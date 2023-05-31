package com.wanderlust.ui.screens.edit_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.usecases.GetUserByName
import com.wanderlust.ui.screens.profile.ProfileSideEffect
import com.wanderlust.ui.screens.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class EditProfileState(
    val userName: String = "",
    val userCity: String = "",
    val userCountry: String = "",
    val userDescription: String = "",
)

sealed interface EditProfileEvent{

    object OnBackBtnClick: EditProfileEvent
    data class OnUsernameChanged(val username: String) : EditProfileEvent
    data class OnUserCityChanged(val userCity: String) : EditProfileEvent
    data class OnUserCountryChanged(val userCountry: String) : EditProfileEvent
    data class OnUserDescriptionChanged(val userDescription: String) : EditProfileEvent
}

sealed interface EditProfileSideEffect {
    object NavigateBack: EditProfileSideEffect
}
@HiltViewModel
class EditProfileViewModel @Inject constructor (
    //savedStateHandle: SavedStateHandle,
    private val getUserByName: GetUserByName,
) : ViewModel() {

    //private val userNameOfProfile: String = savedStateHandle["userName"]!!
    private val userNameOfProfile = "Ivan"
    private val user = getUserByName(userNameOfProfile)

    private val internalState: MutableStateFlow<EditProfileState> = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = internalState

    private val _action = MutableSharedFlow<EditProfileSideEffect?>()
    val action: SharedFlow<EditProfileSideEffect?>
        get() = _action.asSharedFlow()

    init {
        setData()
    }

    private fun setData(){
        internalState.tryEmit(
            internalState.value.copy(
                userName = user.userName,
                userCity = user.userCity,
                userCountry = user.userCountry,
                userDescription = user.userDescription,
            )
        )
    }

    fun event (editProfileEvent: EditProfileEvent){
        when(editProfileEvent){
            EditProfileEvent.OnBackBtnClick -> onBackBtnClick()
            is EditProfileEvent.OnUserCityChanged -> onUserCityChanged(editProfileEvent)
            is EditProfileEvent.OnUserCountryChanged -> onUserCountryChanged(editProfileEvent)
            is EditProfileEvent.OnUserDescriptionChanged -> onUserDescriptionChanged(editProfileEvent)
            is EditProfileEvent.OnUsernameChanged -> onUsernameChanged(editProfileEvent)
        }
    }
    private fun onUsernameChanged(event: EditProfileEvent.OnUsernameChanged){
        internalState.tryEmit(
            internalState.value.copy(
                userName = event.username
            )
        )
    }
    private fun onUserCityChanged(event: EditProfileEvent.OnUserCityChanged){
        internalState.tryEmit(
            internalState.value.copy(
                userCity = event.userCity
            )
        )
    }
    private fun onUserCountryChanged(event: EditProfileEvent.OnUserCountryChanged){
        internalState.tryEmit(
            internalState.value.copy(
                userCountry = event.userCountry
            )
        )
    }
    private fun onUserDescriptionChanged(event: EditProfileEvent.OnUserDescriptionChanged){
        internalState.tryEmit(
            internalState.value.copy(
                userDescription = event.userDescription
            )
        )
    }

    private fun onBackBtnClick(){
        viewModelScope.launch {
            _action.emit(EditProfileSideEffect.NavigateBack)
        }
    }



}