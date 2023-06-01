package com.wanderlust.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun LocationText(city: String?, country: String?, modifier: Modifier = Modifier) {
    if (city == null && country == null) return
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painterResource(R.drawable.baseline_location_on_24),
            contentDescription = "icon_location",
            modifier = Modifier.size(20.dp),
            tint = WanderlustTheme.colors.primaryText
        )
        Text(
            text = if (city == null) country!! else (if (country == null) city else "${city}, ${country}"),
            modifier = Modifier.padding(start = 8.dp),
            style = WanderlustTheme.typography.semibold14,
            color = WanderlustTheme.colors.primaryText
        )
    }
}