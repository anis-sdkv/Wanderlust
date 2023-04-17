package com.wanderlust.ui.screens.sign_in

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.components.auth_screens.*
import com.wanderlust.ui.theme.WanderlustTextStyles

class NavigateProvider : PreviewParameterProvider<() -> Unit> {
    override val values = listOf {}.asSequence()
}

@Preview
@Composable
fun SignInScreen(
    @PreviewParameter(NavigateProvider::class) onNavigateToSignUp: () -> Unit
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
                style = WanderlustTextStyles.AuthorizationMain,
                color = MaterialTheme.colorScheme.background
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
                color = MaterialTheme.colorScheme.background,
                style = WanderlustTextStyles.AuthorizationSemibold,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}