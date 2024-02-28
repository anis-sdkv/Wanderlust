package com.wanderlust.ui.screens.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.usecases.GetCurrentUserUseCase
import com.wanderlust.domain.usecases.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject


@Immutable
data class EditProfileState(
    val userName: String = "",
    val userCity: String = "",
    val userCountry: String = "",
    val userDescription: String = "",
    val showLoadingProgressBar: Boolean = false,
    val showErrorDialog: Boolean = false,
    val errors: PersistentList<String> = persistentListOf()
)

sealed interface EditProfileEvent {
    object OnBackBtnClick : EditProfileEvent
    object OnUpdateButtonClick : EditProfileEvent
    object OnDismissUpdateRequest : EditProfileEvent
    object OnDismissErrorDialog : EditProfileEvent
    data class OnUsernameChanged(val username: String) : EditProfileEvent
    data class OnUserCityChanged(val userCity: String) : EditProfileEvent
    data class OnUserCountryChanged(val userCountry: String) : EditProfileEvent
    data class OnUserDescriptionChanged(val userDescription: String) : EditProfileEvent
}

sealed interface EditProfileSideEffect {
    object NavigateBack : EditProfileSideEffect
}

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<EditProfileState> = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state

    private val _action = MutableSharedFlow<EditProfileSideEffect>()
    val action: SharedFlow<EditProfileSideEffect?>
        get() = _action.asSharedFlow()

    private var currentUser: UserProfile? = null
    private var currentJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        currentJob = null
    }

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase() ?: throw IllegalArgumentException()
            currentUser = user
            _state.emit(
                _state.value.copy(
                    userName = user.username,
                    userCity = user.city ?: "",
                    userCountry = user.country ?: "",
                    userDescription = user.description ?: ""
                )
            )
        }
    }

    fun event(editProfileEvent: EditProfileEvent) {
        when (editProfileEvent) {
            EditProfileEvent.OnBackBtnClick -> onBackBtnClick()
            is EditProfileEvent.OnUserCityChanged -> onUserCityChanged(editProfileEvent)
            is EditProfileEvent.OnUserCountryChanged -> onUserCountryChanged(editProfileEvent)
            is EditProfileEvent.OnUserDescriptionChanged -> onUserDescriptionChanged(editProfileEvent)
            is EditProfileEvent.OnUsernameChanged -> onUsernameChanged(editProfileEvent)
            EditProfileEvent.OnUpdateButtonClick -> onUpdateButtonClick()
            EditProfileEvent.OnDismissErrorDialog -> onDismissErrorDialog()
            EditProfileEvent.OnDismissUpdateRequest -> onDismissUpdateRequest()
        }
    }

    private fun onUsernameChanged(event: EditProfileEvent.OnUsernameChanged) {
        _state.tryEmit(
            _state.value.copy(
                userName = event.username
            )
        )
    }

    private fun onUserCityChanged(event: EditProfileEvent.OnUserCityChanged) {
        _state.tryEmit(
            _state.value.copy(
                userCity = event.userCity
            )
        )
    }

    private fun onUserCountryChanged(event: EditProfileEvent.OnUserCountryChanged) {
        _state.tryEmit(
            _state.value.copy(
                userCountry = event.userCountry
            )
        )
    }

    private fun onDismissErrorDialog() {
        _state.tryEmit(_state.value.copy(showErrorDialog = false))
    }

    private fun onDismissUpdateRequest() {
        currentJob?.cancel()
        _state.tryEmit(_state.value.copy(showLoadingProgressBar = false))
    }


    private fun onUserDescriptionChanged(event: EditProfileEvent.OnUserDescriptionChanged) {
        _state.tryEmit(
            _state.value.copy(
                userDescription = event.userDescription
            )
        )
    }

    private fun onBackBtnClick() {
        viewModelScope.launch {
            _action.emit(EditProfileSideEffect.NavigateBack)
        }
    }

    private fun onUpdateButtonClick() {
        currentJob = viewModelScope.launch {
            val profile = currentUser!!.copy(
                username = state.value.userName,
                city = state.value.userCity.ifEmpty { null },
                country = state.value.userCountry.ifEmpty { null },
                description = state.value.userDescription.ifEmpty { null },
            )
            _state.tryEmit(_state.value.copy(showLoadingProgressBar = true))
            val result = updateUserUseCase(profile)
            _state.tryEmit(_state.value.copy(showLoadingProgressBar = false))

            when (result) {
                is FirestoreActionResult.SuccessResult -> _action.emit(EditProfileSideEffect.NavigateBack)
                is FirestoreActionResult.FailResult -> {
                    _state.emit(
                        _state.value.copy(
                            errors = persistentListOf(result.message ?: "unknown"),
                            showErrorDialog = true
                        )
                    )
                }
            }
        }
    }
}