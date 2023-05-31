package com.wanderlust.ui.components.auth_screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun AuthButton(onClick: () -> Unit, text: String, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = WanderlustTheme.colors.primaryBackground,
            contentColor = WanderlustTheme.colors.primaryText
        )
    ) {
        Text(
            text = text,
            style = WanderlustTheme.typography.semibold14,
            color = WanderlustTheme.colors.primaryText
        )
    }
}