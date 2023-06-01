package com.wanderlust.ui.screens.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wanderlust.ui.R
import com.wanderlust.ui.components.common.ErrorDialog
import com.wanderlust.ui.components.common.LoadingDialog
import com.wanderlust.ui.components.common.ScreenHeader
import com.wanderlust.ui.components.edit_profile_screen.EditProfileTextField
import com.wanderlust.ui.components.edit_profile_screen.EditProfileTextFieldDate
import com.wanderlust.ui.components.edit_profile_screen.EditProfileTextFieldDescription
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            null -> Unit
            EditProfileSideEffect.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    Column(
        Modifier
            .background(WanderlustTheme.colors.primaryBackground)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ScreenHeader(screenName = stringResource(id = R.string.edit_profile)) {
            eventHandler.invoke(EditProfileEvent.OnBackBtnClick)
        }

        Image(
            painterResource(R.drawable.test_user_photo),
            contentDescription = "user_photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 30.dp)
                .size(154.dp)
                .clip(
                    shape = RoundedCornerShape(32.dp)
                ),

            )
        TextButton(
            onClick = {
                // TODO
            },
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.change_photo),
                style = WanderlustTheme.typography.medium13,
                color = WanderlustTheme.colors.accent
            )
        }
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            EditProfileTextField(
                label = stringResource(id = R.string.user_name),
                state.userName
            ) { name -> eventHandler.invoke(EditProfileEvent.OnUsernameChanged(name)) }//eventHandler.invoke(EditProfileEvent.OnUsernameChanged(editProfileState.userName))

            EditProfileTextFieldDate(
                label = stringResource(id = R.string.birthday),
                "",
            )

            EditProfileTextField(
                label = stringResource(id = R.string.country),
                state.userCountry
            ) { country -> eventHandler.invoke(EditProfileEvent.OnUserCountryChanged(country)) }

            EditProfileTextField(
                label = stringResource(id = R.string.city),
                state.userCity
            ) { city -> eventHandler.invoke(EditProfileEvent.OnUserCityChanged(city)) }

            EditProfileTextFieldDescription(
                label = stringResource(id = R.string.about_user),
                state.userDescription
            ) { description -> eventHandler.invoke(EditProfileEvent.OnUserDescriptionChanged(description)) }
        }

        Button(
            onClick = {
                eventHandler.invoke(EditProfileEvent.OnUpdateButtonClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp, bottom = 80.dp)
                .height(42.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WanderlustTheme.colors.accent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.save),
                style = WanderlustTheme.typography.semibold16,
                color = WanderlustTheme.colors.onAccent
            )
        }
    }
}

@Composable
private fun Dialogs(state: EditProfileState, eventHandler: (EditProfileEvent) -> Unit) {
    if (state.showLoadingProgressBar)
        LoadingDialog(
            stringResource(id = R.string.loading_reg),
            onDismiss = { eventHandler.invoke(EditProfileEvent.OnDismissUpdateRequest) }
        )

    if (state.showErrorDialog)
        ErrorDialog(
            title = stringResource(id = R.string.reg_fail),
            errors = state.errors,
            onDismiss = { eventHandler.invoke(EditProfileEvent.OnDismissErrorDialog) }
        )
}