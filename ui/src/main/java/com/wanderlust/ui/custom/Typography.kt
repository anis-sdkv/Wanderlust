package com.wanderlust.ui.custom

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wanderlust.ui.R

private val thin = Font(R.font.montserrat_thin, FontWeight.W100)
private val extraLight = Font(R.font.montserrat_extralight, FontWeight.W200)
private val light = Font(R.font.montserrat_light, FontWeight.W300)
private val regular = Font(R.font.montserrat_regular, FontWeight.W400)
private val medium = Font(R.font.montserrat_medium, FontWeight.W500)
private val semibold = Font(R.font.montserrat_semibold, FontWeight.W600)
private val bold = Font(R.font.montserrat_bold, FontWeight.W700)
private val extraBold = Font(R.font.montserrat_extrabold, FontWeight.W800)
private val black = Font(R.font.montserrat_black, FontWeight.W900)


private val wanderlustFontFamily =
    FontFamily(fonts = listOf(thin, extraLight, light, regular, medium, semibold, bold, extraBold, black))

internal val baseTypography = WanderlustTypography(
    bold40 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
    ),
    bold24 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    bold20 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    bold16 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    semibold20 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    ),
    semibold16 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    semibold14 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    ),
    medium16 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    medium13 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    ),
    medium12 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    regular16 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    extraBold26 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp
    )
)