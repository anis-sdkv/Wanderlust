package com.wanderlust.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
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

val authorizationMain = TextStyle

val wanderlustTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.W300,
        fontSize = 96.sp,
    )
    //TODO если нужно
)

object WanderlustTextStyles {
    val AuthorizationMain = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        color = Color.White
    )
    val AuthorizationInputHint = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = Color.White
    )
    val AuthorizationRegular = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    val AuthorizationSemibold = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    )

    val BottomSheetText = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )

    val ProfileTitleText = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )

    val ProfileLocationText = TextStyle(
    fontFamily = wanderlustFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    )

    val ProfileNumbersText = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp,
    )

    val ProfileMedium13 = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
    )

    val ProfileRouteTitleAndBtnText = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    )

    val ProfileUserInfoText = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    )

    val ProfileRoutesTitleText = TextStyle(
    fontFamily = wanderlustFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    )

    val EditProfileInputText = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )

    val EditProfileInputTextLabel = TextStyle(
        fontFamily = wanderlustFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
    )


}