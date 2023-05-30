package com.wanderlust.ui.permissions

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.wanderlust.ui.R
import com.wanderlust.ui.theme.WanderlustTextStyles

@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    onNavigateBack: () -> Unit,
    updateCurrentLocation: () -> Unit,
    permission: String,
    rationaleMessage: String = stringResource(id = R.string.rationale_message),
) {
    val permissionState = rememberPermissionState(permission)

    HandleRequest(
        permissionState = permissionState,
        updateCurrentLocation = updateCurrentLocation,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale,
                onNavigateBack = onNavigateBack,
            ) { permissionState.launchPermissionRequest() }
        },
        content = {
            /*   Content(
                   text = "PERMISSION GRANTED!",
                   showButton = false
               ) {}*/
        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun HandleRequest(
    permissionState: PermissionState,
    updateCurrentLocation: () -> Unit,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            updateCurrentLocation()
            content()
        }
        is PermissionStatus.Denied -> {
            deniedContent((permissionState.status as PermissionStatus.Denied).shouldShowRationale)
        }
    }
}

@Composable
fun Content(showButton: Boolean = true, onClick: () -> Unit, onNavigateBack: () -> Unit) {
    if (showButton) {
        val enableLocation = remember { mutableStateOf(true) }
        if (enableLocation.value) {
            CustomDialogLocation(
                title = stringResource(id = R.string.location_permission_title),
                desc = stringResource(id = R.string.location_permission_desc),
                enableLocation,
                onNavigateBack = onNavigateBack,
                onClick
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onNavigateBack: () -> Unit,
    onRequestPermission: () -> Unit
) {

    if (shouldShowRationale) {

        AlertDialog(
            onDismissRequest = { onNavigateBack() },
            title = {
                Text(
                    text = stringResource(id = R.string.permission_request),
                    style = WanderlustTextStyles.ProfileRouteTitleAndBtnText
                )
            },
            text = {
                Text(rationaleMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text(stringResource(id = R.string.give_permission))
                }
            }
        )

    }
    else {
        Content(onClick = onRequestPermission, onNavigateBack = onNavigateBack)
    }

}