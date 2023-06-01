package com.wanderlust.ui.components.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun DefaultTextField(label: String, inputValue: String, modifier: Modifier, onChanged: (String) -> Unit){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        var value by remember { mutableStateOf(inputValue) }
        val maxChar = 25

        Text(
            text = label,
            style = WanderlustTheme.typography.semibold16,
            modifier = Modifier
                .padding(start = 18.dp, bottom = 12.dp),
            color = WanderlustTheme.colors.primaryText
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
            textStyle = WanderlustTheme.typography.bold16,
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Text ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth()
                .border(
                    width = (1.5).dp,
                    color = WanderlustTheme.colors.outline,
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = WanderlustTheme.colors.primaryText,
                unfocusedTextColor = WanderlustTheme.colors.primaryText,
                cursorColor = WanderlustTheme.colors.accent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = WanderlustTheme.colors.solid,
                unfocusedContainerColor = WanderlustTheme.colors.solid
            )
        )
    }
}