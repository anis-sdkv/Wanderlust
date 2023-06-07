package com.wanderlust.data.entities

import com.google.firebase.Timestamp

class PlaceEntity {
    var authorId: String? = null
    var authorName: String? = null
    var lat: Double? = null
    var lon: Double? = null
    var placeName: String? = null
    var placeDescription: String? = null
    var city: String? = null
    var country: String? = null
    var comments: List<CommentEntity> = listOf()
    var totalRating: Int? = null
    var ratingCount: Int? = null
    var imagesUrl: List<String> = listOf()
    var tags: List<String> = listOf()
    var createdAt: Timestamp? = null
}