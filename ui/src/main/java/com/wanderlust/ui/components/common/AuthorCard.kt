package com.wanderlust.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun AuthorCard(modifier: Modifier, userName: String, userImage: Painter, onClick: () -> Unit) {

    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WanderlustTheme.colors.solid)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().clickable { onClick() }
        ){
            Row (
                modifier = Modifier
                    .padding(all = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Image(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .size(size = 62.dp),
                    painter = userImage,
                    contentDescription = "user avatar",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(width = 20.dp))
                Text(
                    text = userName,
                    style = WanderlustTheme.typography.semibold16,
                    color = WanderlustTheme.colors.primaryText
                )

            }
        }
    }
}