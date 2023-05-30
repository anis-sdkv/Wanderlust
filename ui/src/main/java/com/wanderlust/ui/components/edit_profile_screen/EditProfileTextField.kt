package com.wanderlust.ui.components.edit_profile_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.screens.edit_profile.EditProfileEvent
import com.wanderlust.ui.theme.WanderlustTextStyles

@Composable
fun EditProfileTextField(label: String, inputValue: String, onChanged: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 10.dp)
    ) {
        var value by remember { mutableStateOf(inputValue) }
        val maxChar = 25

        Text(
            text = label,
            style = WanderlustTextStyles.EditProfileInputTextLabel,
            modifier = Modifier
                .padding(start = 18.dp, bottom = 4.dp)
                .alpha(0.5f)
        )

        TextField(
            value = value,
            onValueChange = {
                if (it.length <= maxChar) {
                    onChanged(it)
                    value = it
                }
            },
            singleLine = true,
            textStyle = WanderlustTextStyles.EditProfileInputText,
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Text ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .border(
                    width = (1.5).dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}