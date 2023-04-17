package com.example.wanderlust.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val default_blue = Color(0xFF196DFF)
private val gradient_blue = Color(0xFF4CA2F1)
private val default_white = Color(0xFFFCFCFC)
private val black = Color(0xFF000000)


val lightColorPalette = lightColorScheme(
    primary = default_blue,
    background = default_white,
    onBackground = black,
    primaryContainer = gradient_blue
)
