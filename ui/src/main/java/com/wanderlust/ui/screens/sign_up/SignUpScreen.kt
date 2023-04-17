package com.wanderlust.ui.screens.sign_up

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.components.auth_screens.*
import com.wanderlust.ui.screens.sign_in.NavigateProvider
import com.wanderlust.ui.theme.WanderlustTextStyles

@Preview
@Composable
fun SignUpScreen(
    @PreviewParameter(NavigateProvider::class) onNavigateToSignIn: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .authGradient(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier,
                style = WanderlustTextStyles.AuthorizationMain,
                color = MaterialTheme.colorScheme.background
            )
        }

        // fields
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            AuthTextField(stringResource(id = R.string.email))
            Spacer(modifier = Modifier.height(20.dp))
            AuthPasswordField(stringResource(id = R.string.password))
            Spacer(modifier = Modifier.height(20.dp))
            AuthPasswordField(stringResource(id = R.string.repeat_pass))
        }

        // Buttons
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            AuthButton(
                onClick = { /*TODO*/ },
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

        AuthBottomText(stringResource(id = R.string.have_acc), stringResource(id = R.string.sign_in)) {
            onNavigateToSignIn()
        }
    }
}