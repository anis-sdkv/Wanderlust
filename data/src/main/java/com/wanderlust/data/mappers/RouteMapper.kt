package com.wanderlust.data.mappers

import com.google.firebase.Timestamp
import com.wanderlust.data.entities.RouteEntity
import com.wanderlust.domain.model.Route

fun RouteEntity.toDomain(id: String): Route =
    Route(
        authorId = this.authorId ?: throw IllegalArgumentException(),
        authorName = this.authorName ?: throw IllegalArgumentException(),
        routeName = this.name ?: throw IllegalArgumentException(),
        routeDescription = this.description ?: throw IllegalArgumentException(),
        createdAt = this.createdAt?.toDate() ?: throw IllegalArgumentException(),
        points = this.points.map { it.toRoutePoint() },
        comments = this.comments.map { it.toComment() },
        tags = this.tags,
        totalRating = this.totalRating ?: throw IllegalArgumentException(),
        ratingCount = this.ratingCount ?: throw IllegalArgumentException(),
        city = this.city ?: throw IllegalArgumentException(),
        country = this.country ?: throw IllegalArgumentException(),
        id = id
    )

fun Route.toEntity(): RouteEntity =
    RouteEntity().apply {
        authorId = this@toEntity.authorId
        authorName = this@toEntity.authorName
        name = this@toEntity.routeName
        description = this@toEntity.routeDescription
        city = this@toEntity.city
        country = this@toEntity.country
        points = this@toEntity.points.map { it.toEntity() }
        comments = this@toEntity.comments.map { it.toEntity() }
        tags = this@toEntity.tags
        createdAt = Timestamp(this@toEntity.createdAt)
        totalRating = this@toEntity.totalRating
        ratingCount = this@toEntity.ratingCount
    }