package com.wanderlust.ui.screens.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.components.edit_profile_screen.EditProfileTextField
import com.wanderlust.ui.components.edit_profile_screen.EditProfileTextFieldDate
import com.wanderlust.ui.components.edit_profile_screen.EditProfileTextFieldDescription
import com.wanderlust.ui.theme.WanderlustTextStyles

@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit
) {

    LazyColumn(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(bottom = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = { onNavigateBack() }
                ) {
                    Image(
                        painterResource(R.drawable.ic_back),
                        contentDescription = "icon_back",
                        contentScale = ContentScale.Crop,
                    )
                }

                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(id = R.string.edit_profile),
                    style = WanderlustTextStyles.ProfileRoutesTitleText,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item {
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
        }
        item {
            TextButton(
                onClick = {
                    // TODO
                },
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.change_photo),
                    style = WanderlustTextStyles.ProfileMedium13,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        item {
            Column(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                EditProfileTextField(label = stringResource(id = R.string.user_name))
                EditProfileTextFieldDate(label = stringResource(id = R.string.birthday))
                EditProfileTextField(label = stringResource(id = R.string.country))
                EditProfileTextField(label = stringResource(id = R.string.city))
                EditProfileTextFieldDescription(label = stringResource(id = R.string.about_user))
            }
        }

        item {
            Button(
                onClick = {
                    // TODO
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp, start = 24.dp, end = 24.dp, bottom = 80.dp)
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    style = WanderlustTextStyles.ProfileRouteTitleAndBtnText,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}