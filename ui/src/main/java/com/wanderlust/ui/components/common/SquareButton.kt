package com.wanderlust.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun SquareButton(
    icon: Int,
    iconColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.solid),
        modifier = modifier
            .padding(start = 8.dp)
            .size(56.dp),
    ) {
        Box(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painterResource(icon),
                modifier = Modifier.size(30.dp),
                contentDescription = "icon",
                tint = iconColor
            )
        }
    }
}