package com.wanderlust.ui.screens.edit_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.usecases.GetUserByName
import com.wanderlust.ui.screens.profile.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


data class EditProfileState(
    val userName: String,
    val userCity: String,
    val userCountry: String,
    val userDescription: String,
)
class EditProfileViewModel (
    savedStateHandle: SavedStateHandle,
    private val getUserByName: GetUserByName,
) : ViewModel() {

    private val userNameOfProfile: String = savedStateHandle["userName"]!!
    private val user = getUserByName(userNameOfProfile)

    private val internalState: MutableStateFlow<EditProfileState> = MutableStateFlow(
        EditProfileState(
            userName = user.userName,
            userCity = user.userCity,
            userCountry = user.userCountry,
            userDescription = user.userDescription,
        )
    )
    val state: StateFlow<EditProfileState> = internalState

}