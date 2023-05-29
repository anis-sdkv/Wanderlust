package com.wanderlust.ui.utils

import android.animation.TimeInterpolator
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

@Composable
fun Float.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

@Composable
fun Int.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

fun TimeInterpolator.toEasing() = Easing {
        x -> getInterpolation(x)
}