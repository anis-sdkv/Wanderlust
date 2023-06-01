package com.wanderlust.domain.model

import com.wanderlust.domain.utils.DateFormatter
import java.util.Date

data class Route(
    val id: String,
    val routeName: String,
    val routeDescription: String,
    val createdAt: Date,
    val points: List<RoutePoint> = listOf(),
    val comments: List<Comment> = listOf(),
    val totalRating: Int = 0,
    val ratingCount: Int = 0
)

data class RoutePoint(
    val lat: Double,
    val lon: Double,
    val placeName: String,
    val placeDescription: String?,
    val imagesUrl: List<String> = listOf()
)
