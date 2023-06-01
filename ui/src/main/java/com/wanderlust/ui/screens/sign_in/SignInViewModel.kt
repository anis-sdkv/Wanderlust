package com.wanderlust.ui.screens.sign_in

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanderlust.domain.action_results.LoginResult
import com.wanderlust.domain.usecases.LoginUseCase
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
data class SignInState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val showLoadingProgressBar: Boolean = false,
    val showErrorDialog: Boolean = false,
    val errors: PersistentList<String> = persistentListOf()
)

sealed interface SignInSideEffect {
    object NavigateProfile : SignInSideEffect
    object NavigateSignUp : SignInSideEffect
}

sealed interface SignInEvent {
    object OnLoginButtonClick : SignInEvent
    object OnSignUpButtonCLick : SignInEvent
    object OnPasswordVisibilityChange : SignInEvent
    object OnDismissLoginRequest : SignInEvent
    object OnDismissErrorDialog : SignInEvent
    data class OnEmailChange(val value: String) : SignInEvent
    data class OnPasswordChange(val value: String) : SignInEvent
}

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val setCurrentUserUseCase: SetCurrentUserUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SignInState> = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state

    private val _action = MutableSharedFlow<SignInSideEffect?>()
    val action: SharedFlow<SignInSideEffect?>
        get() = _action.asSharedFlow()

    private var currentJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
        currentJob = null
    }

    fun event(event: SignInEvent) {
        when (event) {
            SignInEvent.OnPasswordVisibilityChange -> onPasswordVisibilityChange()
            SignInEvent.OnDismissErrorDialog -> onDismissErrorDialog()
            SignInEvent.OnDismissLoginRequest -> onDismissLoginRequest()
            is SignInEvent.OnEmailChange -> onEmailChange(event.value)
            is SignInEvent.OnPasswordChange -> onPasswordChange(event.value)
            SignInEvent.OnLoginButtonClick -> onLoginButtonClick()
            SignInEvent.OnSignUpButtonCLick -> onSignUpButtonCLick()
        }
    }

    private fun onEmailChange(email: String) {
        _state.tryEmit(_state.value.copy(email = email))
    }

    private fun onPasswordChange(password: String) {
        _state.tryEmit(_state.value.copy(password = password))
    }

    private fun onPasswordVisibilityChange() {
        _state.tryEmit(_state.value.copy(passwordVisible = !_state.value.passwordVisible))
    }

    private fun onDismissErrorDialog() {
        _state.tryEmit(_state.value.copy(showErrorDialog = false))
    }

    private fun onDismissLoginRequest() {
        currentJob?.cancel()
        _state.tryEmit(_state.value.copy(showLoadingProgressBar = false))
    }

    private fun onSignUpButtonCLick() {
        currentJob?.cancel()
        viewModelScope.launch { _action.emit(SignInSideEffect.NavigateSignUp) }
    }

    private fun onLoginButtonClick() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val errors = mutableListOf<String>()
            _state.emit(_state.value.copy(showLoadingProgressBar = true))
            val result = if (validateFields(errors)) with(state.value) {
                loginUseCase(email, password)
            }
            else LoginResult.FailLogin()
            _state.emit(_state.value.copy(showLoadingProgressBar = false))

            when (result) {
                is LoginResult.SuccessLogin -> {
                    setCurrentUserUseCase(result.userId)
                    _action.emit(SignInSideEffect.NavigateProfile)
                }

                is LoginResult.FailLogin -> {
                    result.errorMessage?.let { errors.add(it) }
                    _state.emit(_state.value.copy(showErrorDialog = true, errors = errors.toPersistentList()))
                }
            }
        }
    }

    private fun validateFields(errors: MutableList<String>): Boolean {
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

        return passValidate
    }
}