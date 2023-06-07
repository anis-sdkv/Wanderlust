package com.wanderlust.ui.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun SwitchButton(onClick: () -> Unit, pressed: Boolean, text: String, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (pressed)
                WanderlustTheme.colors.secondaryBackground
            else
                WanderlustTheme.colors.accent
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = WanderlustTheme.typography.semibold16,
            color = if (pressed) WanderlustTheme.colors.secondaryText else WanderlustTheme.colors.onAccent,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}