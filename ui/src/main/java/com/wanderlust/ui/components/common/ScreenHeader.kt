package com.wanderlust.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun ScreenHeader(screenName: String, onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onNavigateBack() }
        ) {
            Image(
                painterResource(R.drawable.ic_back),
                contentDescription = "icon_back",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(WanderlustTheme.colors.primaryText)
            )
        }

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = screenName,
            style = WanderlustTheme.typography.bold20,
            color = WanderlustTheme.colors.primaryText,
        )
    }
}
