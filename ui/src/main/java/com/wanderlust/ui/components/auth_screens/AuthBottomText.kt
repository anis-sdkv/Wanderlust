package com.wanderlust.ui.components.auth_screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.theme.WanderlustTextStyles

@Composable
fun AuthBottomText(descriptionText: String, btnText: String, onClick: () -> Unit) {
    Row(
        Modifier.padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            descriptionText,
            style = WanderlustTextStyles.AuthorizationRegular,
            color = MaterialTheme.colorScheme.background
        )
        TextButton(onClick = onClick) {
            Text(
                btnText,
                style = WanderlustTextStyles.AuthorizationRegular,
                color = MaterialTheme.colorScheme.background,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}