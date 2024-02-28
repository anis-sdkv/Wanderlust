package com.wanderlust.ui.screens.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.wanderlust.ui.settings.LocalSettingsEventBus

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
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
            SignUpSideEffect.NavigateProfile -> navController.navigate(BottomNavigationItem.Profile.graph)
            SignUpSideEffect.NavigateSignIn -> navController.navigateUp()
            else -> Unit
        }
    }

    SignUpMainContent(state = state, eventHandler = eventHandler)
    Dialogs(state = state, eventHandler = eventHandler)

    DisposableEffect(Unit) {
        onDispose {
            settingsEventBus.updateDarkMode(originDarkMode)
        }
    }
}

@Composable
private fun SignUpMainContent(state: SignUpState, eventHandler: (SignUpEvent) -> Unit) {
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
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier,
                style = WanderlustTheme.typography.bold40,
                color = WanderlustTheme.colors.primaryBackground
            )
        }

        // fields
        Column {
            AuthTextField(
                stringResource(id = R.string.user_name),
                state.username
            ) { eventHandler.invoke(SignUpEvent.OnUsernameChange(it)) }

            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(
                stringResource(id = R.string.email),
                state.email
            ) { eventHandler.invoke(SignUpEvent.OnEmailChange(it)) }

            Spacer(modifier = Modifier.height(20.dp))
            AuthPasswordField(
                stringResource(id = R.string.password),
                state.password,
                { eventHandler.invoke(SignUpEvent.OnPasswordChange(it)) },
                state.passwordVisible,
                { eventHandler.invoke(SignUpEvent.OnPasswordVisibilityChange) })

            Spacer(modifier = Modifier.height(20.dp))
            AuthPasswordField(
                stringResource(id = R.string.repeat_pass),
                state.confirmPassword,
                { eventHandler.invoke(SignUpEvent.OnConfirmPasswordChange(it)) },
                state.passwordVisible,
                { eventHandler.invoke(SignUpEvent.OnPasswordVisibilityChange) })
        }

        // Buttons
        Column {
            AuthButton(
                onClick = { eventHandler.invoke(SignUpEvent.OnRegisterButtonClick) },
                text = stringResource(id = R.string.sign_up_btn),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(60.dp)
                    .align(Alignment.CenterHorizontally)
            )
            DecoratedText(text = stringResource(id = R.string.reg_via_social), Modifier.padding(vertical = 12.dp))
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

        AuthBottomText(
            stringResource(id = R.string.have_acc),
            stringResource(id = R.string.sign_in)
        ) { eventHandler.invoke(SignUpEvent.OnSignInButtonCLick) }
    }
}

@Composable
private fun Dialogs(state: SignUpState, eventHandler: (SignUpEvent) -> Unit) {
    if (state.showLoadingProgressBar)
        LoadingDialog(
            stringResource(id = R.string.loading_reg),
            onDismiss = { eventHandler.invoke(SignUpEvent.OnDismissRegisterRequest) }
        )

    if (state.showErrorDialog)
        ErrorDialog(
            title = stringResource(id = R.string.reg_fail),
            errors = state.errors,
            onDismiss = { eventHandler.invoke(SignUpEvent.OnDismissErrorDialog) }
        )
}