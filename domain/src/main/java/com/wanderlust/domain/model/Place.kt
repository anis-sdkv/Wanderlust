package com.wanderlust.domain.model

import java.util.Date

data class Place(
    val authorId: String?,
    val authorName: String?,
    val lat: Double,
    val lon: Double,
    val placeDescription: String,
    val placeName: String,
    val city: String = "undefined",
    val country: String = "undefined",
    val comments: List<Comment> = listOf(),
    val createdAt: Date,
    val totalRating: Int = 0,
    val ratingCount: Int = 0,
    val imagesUrl: List<String> = listOf(),
    val tags: List<String> = listOf(),
    val id: String? = null,
)