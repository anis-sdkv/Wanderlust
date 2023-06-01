package com.wanderlust.domain.model

import java.util.Date

data class Place(
    val id: String,
    val lat: Double,
    val lon: Double,
    val placeName: String,
    val placeDescription: String,
    val comments: List<Comment> = listOf(),
    val createdAt: Date,
    val totalRating: Int = 0,
    val ratingCount: Int = 0,
    val imagesUrl: List<String> = listOf()
)