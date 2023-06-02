package com.wanderlust.data.mappers

import com.google.firebase.Timestamp
import com.wanderlust.data.entities.RouteEntity
import com.wanderlust.domain.model.Route
import javax.inject.Inject

class RouteMapper @Inject constructor(
    private val placeMapper: PlaceMapper
) {
    fun map(entity: RouteEntity): Route =
        Route(
            routeName = entity.name ?: throw IllegalArgumentException(),
            routeDescription = entity.description ?: throw IllegalArgumentException(),
            createdAt = entity.createdAt?.toDate() ?: throw IllegalArgumentException(),
            points = entity.points.map { it.toRoutePoint() },
            comments = entity.comments.map { it.toComment() },
            tags = entity.tags,
            totalRating = entity.totalRating ?: throw IllegalArgumentException(),
            ratingCount = entity.ratingCount ?: throw IllegalArgumentException(),
            city = entity.city ?: throw IllegalArgumentException(),
            country = entity.country ?: throw IllegalArgumentException()
        )

    fun map(route: Route): RouteEntity =
        RouteEntity().apply {
            name = route.routeName
            description = route.routeDescription
            city = route.city
            country = route.country
            points = route.points.map { it.toEntity() }
            comments = route.comments.map { it.toEntity() }
            tags = route.tags
            createdAt = Timestamp(route.createdAt)
            totalRating = route.totalRating
            ratingCount = route.ratingCount
        }
}