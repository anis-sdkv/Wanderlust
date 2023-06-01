package com.wanderlust.data.entities

import com.google.firebase.Timestamp
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.model.RoutePoint

class RouteEntity {
    var id: String? = null
    var routeName: String? = null
    var routeDescription: String? = null
    var points: List<RoutePoint> = listOf()
    var comments: List<Comment> = listOf()
    var totalRating: Int? = null
    var ratingCount: Int? = null
    var createdAt: Timestamp? = null
}