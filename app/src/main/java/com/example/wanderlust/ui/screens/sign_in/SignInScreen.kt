package com.example.wanderlust.ui.screens.sign_in

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.wanderlust.R
import com.example.wanderlust.ui.theme.WanderlustTextStyles

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SignInScreen() {
    ConstraintLayout(
        Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
    ) {
        val (text, column) = createRefs()
        Text(
            text = stringResource(id = R.string.sign_in),
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            style = WanderlustTextStyles.AuthorizationMain
        )

        Column(
            modifier = Modifier.constrainAs(column) {
                top.linkTo(text.bottom, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            var value by remember { mutableStateOf("Hello") }
            Text(
                text = stringResource(id = R.string.email),
                style = WanderlustTextStyles.AuthorizationInputHint
            )
            TextField(
                value = value,
                onValueChange = { value = it },
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                singleLine = true,
                textStyle = WanderlustTextStyles.AuthorizationInputInnerText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}