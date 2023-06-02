package com.wanderlust.data.entities

import com.google.firebase.Timestamp

class RouteEntity {
    var authorId: String? = null
    var authorName: String? = null
    var name: String? = null
    var description: String? = null
    var city: String? = null
    var country: String? = null
    var points: List<RoutePointEntity> = listOf()
    var comments: List<CommentEntity> = listOf()
    var tags: List<String> = listOf()
    var totalRating: Int? = null
    var ratingCount: Int? = null
    var createdAt: Timestamp? = null
}

class RoutePointEntity {
    var lat: Double? = null
    var lon: Double? = null
    var name: String? = null
    var description: String? = null
    var imagesUrl: List<String> = listOf()
}