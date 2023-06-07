package com.wanderlust.ui.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun CustomDropDownItem(text: String, onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                style = WanderlustTheme.typography.semibold14,
                color = WanderlustTheme.colors.secondaryText,
                textAlign = TextAlign.Center
            )
        },
        onClick = onClick
    )
}