package com.wanderlust.ui.components.auth_screens

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun SocialMediaAuthButton(onClick: () -> Unit, painter: Painter, modifier: Modifier = Modifier) {
    Button(
        onClick = { /*TODO*/ },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = WanderlustTheme.colors.primaryBackground,
        )
    ) {
        Icon(
            contentDescription = "icon",
            modifier = Modifier.size(40.dp),
            painter = painter,
            tint = Color.Unspecified
        )
    }
}
