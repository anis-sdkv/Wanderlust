package com.wanderlust.data.entities

import com.google.firebase.Timestamp
import com.wanderlust.domain.model.Comment

class PlaceEntity {
    var id: String? = null
    var lat: Double? = null
    var lon: Double? = null
    var placeName: String? = null
    var placeDescription: String? = null
    var comments: List<Comment> = listOf()
    var totalRating: Int? = null
    var ratingCount: Int? = null
    var imagesUrl: List<String> = listOf()
    var createdAt: Timestamp? = null
}