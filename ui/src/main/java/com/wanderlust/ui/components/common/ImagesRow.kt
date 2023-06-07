package com.wanderlust.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun ImagesRow (
    modifier: Modifier,
    gradientColor: Color,
    isAddingEnable: Boolean,
    imagesUrl: List<String>,
    onAddImageClick: () -> Unit
) {
    Box(
        modifier
            .height(120.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState())
        ){
            imagesUrl.forEach {
                Image(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(120.dp)
                        .clip(shape = RoundedCornerShape(16.dp)),
                    painter = painterResource(id = R.drawable.test_user_photo),
                    contentDescription = "Place Image"
                )
            }
            if (isAddingEnable){
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .alpha(0.2f)
                        .background(color = WanderlustTheme.colors.accent)
                        .clickable { onAddImageClick() }
                ){
                    Icon(
                        painterResource(id = R.drawable.ic_add_a_photo),
                        contentDescription = "icon_send",
                        modifier = Modifier
                            .size(54.dp)
                            .align(Alignment.Center),
                        WanderlustTheme.colors.onAccent
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(50.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            gradientColor
                        )
                    )
                )
        )
    }
}