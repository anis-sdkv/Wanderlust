package com.wanderlust.ui.components.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun ErrorDialog(title: String, errors: List<String>, onDismiss: () -> Unit) {
    AlertDialog(
        containerColor = WanderlustTheme.colors.primaryBackground,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = title,
                style = WanderlustTheme.typography.semibold16,
                color = WanderlustTheme.colors.primaryText
            )
        },
        text = {
            LazyColumn {
                items(errors.size) {
                    Text(
                        text = errors[it],
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = WanderlustTheme.typography.medium12,
                        color = WanderlustTheme.colors.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "OK",
                    style = WanderlustTheme.typography.semibold14,
                    color = WanderlustTheme.colors.accent
                )
            }
        },
        onDismissRequest = onDismiss,
    )
}