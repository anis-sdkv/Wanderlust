package com.wanderlust.ui.screens.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.components.auth_screens.AuthBottomText
import com.wanderlust.ui.components.auth_screens.AuthButton
import com.wanderlust.ui.components.auth_screens.AuthPasswordField
import com.wanderlust.ui.components.auth_screens.AuthTextField
import com.wanderlust.ui.components.auth_screens.DecoratedText
import com.wanderlust.ui.components.auth_screens.SocialMediaAuthButton
import com.wanderlust.ui.components.auth_screens.authGradient
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit
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
                text = stringResource(id = R.string.sign_in),
                modifier = Modifier,
                style = WanderlustTheme.typography.bold40,
                color = WanderlustTheme.colors.primaryBackground
            )
        }

        // fields
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            AuthTextField(stringResource(id = R.string.email))
            Spacer(modifier = Modifier.height(20.dp))
            AuthPasswordField(stringResource(id = R.string.password))
            ForgotPassButton {
                //TODO
            }
        }

        // Buttons
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            AuthButton(
                onClick = { /*TODO*/ },
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
            onNavigateToSignUp()
        }
    }
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