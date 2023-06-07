package com.wanderlust.ui.utils

import android.animation.TimeInterpolator
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import com.wanderlust.domain.model.Place
import com.wanderlust.domain.model.Route
import kotlin.math.round

@Composable
fun Float.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

@Composable
fun Int.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

fun TimeInterpolator.toEasing() = Easing { x ->
    getInterpolation(x)
}

fun Route.calculateRating() =
    round(this.totalRating.toFloat() / this.ratingCount * 10) / 10f
fun Place.calculateRating() =
    round(this.totalRating.toFloat() / this.ratingCount * 10) / 10f