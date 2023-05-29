package com.wanderlust.ui.components.edit_profile_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun EditProfileTextFieldDescription(label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 10.dp)
    ) {

        var value by remember { mutableStateOf("") }
        val maxChar = 300
        val maxLines = 15

        Text(
            text = label,
            style = WanderlustTheme.typography.bold16,
            modifier = Modifier
                .padding(start = 18.dp, bottom = 4.dp)
                .alpha(0.5f)
        )

        TextField(
            value = value,
            onValueChange = { if (it.length <= maxChar) value = it },
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(8.dp),
            textStyle = WanderlustTheme.typography.medium16,
            supportingText = {
                Text(
                    text = "${value.length} / $maxChar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    textAlign = TextAlign.End,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = (1.5).dp,
                    color = WanderlustTheme.colors.outline,
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = TextFieldDefaults.colors(
                cursorColor = WanderlustTheme.colors.accent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = WanderlustTheme.colors.solid,
                unfocusedContainerColor = WanderlustTheme.colors.solid,
            )
        )
    }
}