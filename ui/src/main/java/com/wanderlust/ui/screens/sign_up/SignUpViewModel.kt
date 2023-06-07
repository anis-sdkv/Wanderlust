package com.wanderlust.ui.screens.sign_up

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.action_results.RegisterResult
import com.wanderlust.domain.usecases.RegisterUseCase
import com.wanderlust.domain.usecases.SetCurrentUserUseCase
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
import javax.inject.Inject

@Immutable
data class SignUpState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val showLoadingProgressBar: Boolean = false,
    val showErrorDialog: Boolean = false,
    val errors: PersistentList<String> = persistentListOf()
)

sealed interface SignUpSideEffect {
    object NavigateProfile : SignUpSideEffect
    object NavigateSignIn : SignUpSideEffect
}

sealed interface SignUpEvent {
    object OnRegisterButtonClick : SignUpEvent
    object OnSignInButtonCLick : SignUpEvent
    object OnPasswordVisibilityChange : SignUpEvent
    object OnDismissRegisterRequest : SignUpEvent
    object OnDismissErrorDialog : SignUpEvent
    data class OnUsernameChange(val value: String) : SignUpEvent
    data class OnEmailChange(val value: String) : SignUpEvent
    data class OnPasswordChange(val value: String) : SignUpEvent
    data class OnConfirmPasswordChange(val value: String) : SignUpEvent
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val setCurrentUserUseCase: SetCurrentUserUseCase,
    private val register: RegisterUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SignUpState> = MutableStateFlow(
        SignUpState()
    )
    val state: StateFlow<SignUpState> = _state

    private val _action = MutableSharedFlow<SignUpSideEffect?>()
    val action: SharedFlow<SignUpSideEffect?>
        get() = _action.asSharedFlow()

    private var currentJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
        currentJob = null
    }

    fun event(event: SignUpEvent) {
        when (event) {
            SignUpEvent.OnRegisterButtonClick -> onRegisterButtonClick()
            SignUpEvent.OnSignInButtonCLick -> onSignInButtonClick()
            SignUpEvent.OnPasswordVisibilityChange -> onPasswordVisibilityChange()
            SignUpEvent.OnDismissErrorDialog -> onDismissErrorDialog()
            SignUpEvent.OnDismissRegisterRequest -> onDismissRegisterRequest()
            is SignUpEvent.OnUsernameChange -> onUsernameChange(event.value)
            is SignUpEvent.OnEmailChange -> onEmailChange(event.value)
            is SignUpEvent.OnPasswordChange -> onPasswordChange(event.value)
            is SignUpEvent.OnConfirmPasswordChange -> onConfirmPasswordChange(event.value)
        }
    }

    private fun onUsernameChange(username: String) {
        _state.tryEmit(_state.value.copy(username = username))
    }

    private fun onEmailChange(email: String) {
        _state.tryEmit(_state.value.copy(email = email))
    }

    private fun onPasswordChange(password: String) {
        _state.tryEmit(_state.value.copy(password = password))
    }

    private fun onConfirmPasswordChange(password: String) {
        _state.tryEmit(_state.value.copy(confirmPassword = password))
    }

    private fun onPasswordVisibilityChange() {
        _state.tryEmit(_state.value.copy(passwordVisible = !_state.value.passwordVisible))
    }

    private fun onDismissErrorDialog() {
        _state.tryEmit(_state.value.copy(showErrorDialog = false))
    }

    private fun onDismissRegisterRequest() {
        currentJob?.cancel()
        _state.tryEmit(_state.value.copy(showLoadingProgressBar = false))
    }

    private fun onSignInButtonClick() {
        currentJob?.cancel()
        viewModelScope.launch {
            _action.emit(SignUpSideEffect.NavigateSignIn)
        }
    }

    private fun onRegisterButtonClick() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val errors = mutableListOf<String>()
            _state.emit(_state.value.copy(showLoadingProgressBar = true))
            val result =
                if (validateFields(errors)) with(state.value) {
                    register(username, email, password)
                }
                else RegisterResult.FailRegister()
            _state.emit(_state.value.copy(showLoadingProgressBar = false))

            when (result) {
                is RegisterResult.SuccessRegister -> {
                    setCurrentUserUseCase(result.id)
                    _action.emit(SignUpSideEffect.NavigateProfile)
                }

                is RegisterResult.FailRegister -> {
                    result.errorMessage?.let { errors.add(it) }
                    _state.emit(_state.value.copy(showErrorDialog = true, errors = errors.toPersistentList()))
                }
            }
        }
    }

    private fun validateFields(errors: MutableList<String>): Boolean {
        if (state.value.username.length < 3) {
            errors.add("Имя пользователя должно состоять не менее чем из 3 символов.")
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.value.email).matches()) {
            errors.add("Неверный формат почты.")
            return false
        }

        var passValidate = true
        val password = state.value.password
        if (!password.matches(Regex(".*[a-z].*"))) {
            errors.add("Пароль должен содержать хотя бы одну маленькую букву.")
            passValidate = false
        }

        if (!password.matches(Regex(".*[A-Z].*"))) {
            errors.add("Пароль должен содержать хотя бы одну большую букву.")
            passValidate = false
        }

        if (!password.matches(Regex(".*\\d.*"))) {
            passValidate = false
            errors.add("Пароль должен содержать хотя бы одну цифру.")
        }

        if (password.length < 8) {
            passValidate = false
            errors.add("Пароль должен содержать минимум 8 символов.")
        }

        if (password != state.value.confirmPassword) {
            passValidate = false
            errors.add("Пароли не совпадают.")
        }

        return passValidate
    }
}