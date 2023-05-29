package com.wanderlust.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

//@Composable
//fun WanderlustTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val systemUiController = rememberSystemUiController()
//    val colors = if (darkTheme) {
//        systemUiController.setSystemBarsColor(
//            color = Color.Transparent
//        )
//        //TODO
//        lightColorPalette
//    } else {
//        systemUiController.setSystemBarsColor(
//            color = lightColorPalette.primary
//        )
//        lightColorPalette
//    }
//
//    MaterialTheme(
//        colorScheme = colors,
//        content = content
//    )
//}