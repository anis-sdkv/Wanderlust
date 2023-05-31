package com.wanderlust.ui.custom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class WanderlustColors(
    val accent: Color,
    val tint: Color,
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val surface: Color, // Для кнопки Отписаться/Редактировать Профиль
    val solid: Color, // Для заполнения полей
    val outline: Color,
    val error: Color
)

data class WanderlustTypography(
    val bold40: TextStyle,
    val bold24: TextStyle,
    val bold20: TextStyle,
    val bold16: TextStyle,
    val semibold16: TextStyle,
    val semibold14: TextStyle,
    val medium16: TextStyle,
    val medium13: TextStyle,
    val medium12: TextStyle,
    val regular16: TextStyle,
    val extraBold26: TextStyle
)

data class WanderlustShape(
    val padding: Dp,
    val cornersStyle: Shape
)

object WanderlustTheme {
    val colors: WanderlustColors
        @Composable
        get() = LocalWanderlustColors.current

    val typography: WanderlustTypography
        @Composable
        get() = LocalWanderlustTypography.current

    val shapes: WanderlustShape
        @Composable
        get() = LocalWanderlustShape.current
}

val LocalWanderlustColors = staticCompositionLocalOf<WanderlustColors> {
    error("No colors provided")
}

val LocalWanderlustTypography = staticCompositionLocalOf<WanderlustTypography> {
    error("No font provided")
}

val LocalWanderlustShape = staticCompositionLocalOf<WanderlustShape> {
    error("No shapes provided")
}