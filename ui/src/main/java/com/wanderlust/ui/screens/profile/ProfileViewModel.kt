package com.wanderlust.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.usecases.GetUserByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class ProfileState(
    val isUserAuthorized: Boolean,
    val isMyProfile: Boolean,
    val isSubscribe: Boolean,
    val userName: String,
    val userCity: String,
    val userCountry: String,
    val userDescription: String,
    val userRoutes: List<Route>,
    val userNumberOfSubscribers: Int,
    val userNumberOfSubscriptions: Int,
    val userNumberOfRoutes: Int
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    private val getUserByName: GetUserByName,
) : ViewModel() {

    //    private val userNameOfProfile: String = savedStateHandle["userName"]!!
    private val userNameOfProfile: String = "Ivan"
    private val user = getUserByName(userNameOfProfile)

    private val isSubscribe = (user.userSubscriptions.find { it.userName == userNameOfProfile }) != null

    private val internalState: MutableStateFlow<ProfileState> = MutableStateFlow(
        ProfileState(
            isUserAuthorized = false,
            isMyProfile = false, //(userNameOfProfile == user.userName),
            isSubscribe = isSubscribe,
            userName = user.userName,
            userCity = user.userCity,
            userCountry = user.userCountry,
            userDescription = user.userDescription,
            userRoutes = user.userRoutes,
            userNumberOfSubscribers = user.userSubscribers.size,
            userNumberOfSubscriptions = user.userSubscriptions.size,
            userNumberOfRoutes = user.userSubscriptions.size
        )
    )
    val state: StateFlow<ProfileState> = internalState
}