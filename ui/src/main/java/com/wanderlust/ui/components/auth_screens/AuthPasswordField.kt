package com.wanderlust.ui.components.auth_screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthPasswordField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisibleChange: () -> Unit
) {
    Column {

        Text(
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
            text = label,
            style = WanderlustTheme.typography.semibold16,
            color = WanderlustTheme.colors.primaryBackground
        )

        TextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = WanderlustTheme.typography.semibold16,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = WanderlustTheme.colors.primaryBackground
            ),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Done else Icons.Filled.PlayArrow
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = onVisibleChange) {
                    Icon(imageVector = image, description)
                }
            }
        )
    }
}