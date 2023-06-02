package com.wanderlust.domain.model

import java.util.Date

data class Route(
    val routeName: String,
    val routeDescription: String,
    val city: String = "undefined",
    val country: String = "undefined",
    val createdAt: Date,
    val points: List<RoutePoint> = listOf(),
    val comments: List<Comment> = listOf(),
    val tags: List<String> = listOf(),
    val totalRating: Int = 0,
    val ratingCount: Int = 0,
    val id: String? = null,
)

data class RoutePoint(
    var lat: Double,
    var lon: Double,
    var name: String,
    var description: String?,
    var imagesUrl: List<String> = listOf()
)
