package com.wanderlust.ui.components.common

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme


@Composable
fun DefaultDescriptionField(label: String, inputValue: String, modifier: Modifier, onChanged: (String) -> Unit){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        var value by remember { mutableStateOf(inputValue) }
        val maxChar = 300
        val maxLines = 15

        Text(
            text = label,
            style = WanderlustTheme.typography.semibold16,
            modifier = Modifier
                .padding(start = 18.dp, bottom = 12.dp),
            color = WanderlustTheme.colors.primaryText
        )

        TextField(
            value = value,
            onValueChange = { if (it.length <= maxChar)
                onChanged(it)
                value = it
            },
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
                    color = WanderlustTheme.colors.secondaryText
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(WanderlustTheme.colors.solid)
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
                unfocusedContainerColor = WanderlustTheme.colors.solid,
            )
        )
    }
}