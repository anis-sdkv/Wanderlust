package com.wanderlust.ui.screens.sign_in

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wanderlust.ui.R
import com.wanderlust.ui.components.auth_screens.AuthBottomText
import com.wanderlust.ui.components.auth_screens.AuthButton
import com.wanderlust.ui.components.auth_screens.AuthPasswordField
import com.wanderlust.ui.components.auth_screens.AuthTextField
import com.wanderlust.ui.components.auth_screens.DecoratedText
import com.wanderlust.ui.components.auth_screens.SocialMediaAuthButton
import com.wanderlust.ui.components.auth_screens.authGradient
import com.wanderlust.ui.components.common.ErrorDialog
import com.wanderlust.ui.components.common.LoadingDialog
import com.wanderlust.ui.custom.WanderlustTheme
import com.wanderlust.ui.navigation.BottomNavigationItem
import com.wanderlust.ui.navigation.graphs.AuthScreen
import com.wanderlust.ui.screens.sign_up.SignUpEvent
import com.wanderlust.ui.settings.LocalSettingsEventBus

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val settingsEventBus = LocalSettingsEventBus.current
    val currentSettings = settingsEventBus.currentSettings.collectAsState().value
    val originDarkMode = currentSettings.isDarkMode

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(Unit) {
        settingsEventBus.updateDarkMode(false)
    }

    LaunchedEffect(action) {
        when (action) {
            SignInSideEffect.NavigateProfile -> navController.navigate(BottomNavigationItem.Profile.graph)
            SignInSideEffect.NavigateSignUp -> navController.navigate(AuthScreen.SignUp.route)
            else -> Unit
        }
    }

    SignInMainContent(state = state, eventHandler = eventHandler)
    Dialogs(state = state, eventHandler = eventHandler)

    DisposableEffect(Unit) {
        onDispose {
            settingsEventBus.updateDarkMode(originDarkMode)
        }
    }
}

@Composable
fun SignInMainContent(state: SignInState, eventHandler: (SignInEvent) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .authGradient()
            .padding(horizontal = 20.dp)
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = stringResource(id = R.string.sign_in),
                modifier = Modifier,
                style = WanderlustTheme.typography.bold40,
                color = WanderlustTheme.colors.primaryBackground
            )
        }

        // fields
        Column {
            AuthTextField(stringResource(id = R.string.email), state.email) {
                eventHandler.invoke(SignInEvent.OnEmailChange(it))
            }

            Spacer(modifier = Modifier.height(20.dp))
            AuthPasswordField(
                stringResource(id = R.string.password), state.password,
                onChange = { eventHandler.invoke(SignInEvent.OnPasswordChange(it)) },
                state.passwordVisible,
                onVisibleChange = { eventHandler.invoke(SignInEvent.OnPasswordVisibilityChange) }
            )

            ForgotPassButton {
                //TODO
            }
        }

        // Buttons
        Column {
            AuthButton(
                onClick = { eventHandler.invoke(SignInEvent.OnLoginButtonClick) },
                text = stringResource(id = R.string.sign_in_btn),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(60.dp)
                    .align(Alignment.CenterHorizontally)
            )
            DecoratedText(text = stringResource(id = R.string.or), Modifier.padding(vertical = 12.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SocialMediaAuthButton(
                    {}, painterResource(id = R.drawable.ic_vk),
                    Modifier
                        .height(60.dp)
                        .weight(1f)
                )
                SocialMediaAuthButton(
                    {}, painterResource(id = R.drawable.ic_google),
                    Modifier
                        .height(60.dp)
                        .weight(1f)
                )
            }
        }

        AuthBottomText(stringResource(id = R.string.forgot_pass), stringResource(id = R.string.reg)) {
            eventHandler.invoke(SignInEvent.OnSignUpButtonCLick)
        }
    }
}

@Composable
private fun Dialogs(state: SignInState, eventHandler: (SignInEvent) -> Unit) {

    if (state.showLoadingProgressBar)
        LoadingDialog(
            stringResource(id = R.string.loading_login),
            onDismiss = { eventHandler.invoke(SignInEvent.OnDismissLoginRequest) }
        )

    if (state.showErrorDialog)
        ErrorDialog(
            title = stringResource(id = R.string.login_fail),
            errors = state.errors,
            onDismiss = { eventHandler.invoke(SignInEvent.OnDismissErrorDialog) }
        )
}

@Composable
fun ForgotPassButton(onclick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(
            onClick = onclick,
            modifier = Modifier
                .align(Alignment.CenterEnd),
        ) {
            Text(
                text = stringResource(id = R.string.forgot_pass),
                color = WanderlustTheme.colors.primaryBackground,
                style = WanderlustTheme.typography.medium16,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}