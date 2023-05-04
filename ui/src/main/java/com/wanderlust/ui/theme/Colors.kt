package com.wanderlust.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val default_blue = Color(0xFF196DFF)
private val gradient_blue = Color(0xFF4CA2F1)
private val default_white = Color(0xFFFCFCFC)
private val black = Color(0xFF000000)
private val light_gray = Color(0xEEEEEEEE)
private val dark_gray = Color(0xFFC5C5C5)
private val container_gray = Color(0xFFF2F2F2)
private val total_white = Color(0xFFFFFFFF)


val lightColorPalette = lightColorScheme(
    primary = default_blue,
    background = default_white,
    onBackground = black,
    primaryContainer = gradient_blue,
    outline = light_gray,
    surface = dark_gray, // Для кнопки Отписаться/Редактировать Профиль
    surfaceVariant = total_white // Для заполнения полей

)
