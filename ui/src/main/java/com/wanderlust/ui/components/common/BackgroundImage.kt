package com.wanderlust.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import com.wanderlust.ui.R
import com.wanderlust.ui.custom.WanderlustTheme

@Composable
fun AnimatedBackgroundImage(columnState: LazyListState) {
    var imageHeight by remember {
        mutableStateOf(500)
    }

    val imageTranslationY by remember {
        derivedStateOf { columnState.firstVisibleItemScrollOffset * .6f }
    }

    val imageVisibility by remember {
        derivedStateOf { columnState.firstVisibleItemScrollOffset / imageHeight.toFloat() }
    }
    val gradientColor = WanderlustTheme.colors.secondaryText
    Image(
        painterResource(R.drawable.test_user_photo),
        contentDescription = "user_photo",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .graphicsLayer {
                //анимация и изменение непрозрачности изображения при скролле:
                translationY = imageTranslationY
                alpha = 1f - imageVisibility
            }
            .drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, gradientColor),
                    startY = size.height / 3 * 2,
                    endY = size.height
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.Multiply)
                }
            }
            .onGloballyPositioned {
                imageHeight = it.size.height
            }
    )
}