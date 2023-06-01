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
            entity.id ?: throw IllegalArgumentException(),
            entity.routeName ?: throw IllegalArgumentException(),
            entity.routeDescription ?: throw IllegalArgumentException(),
            entity.createdAt?.toDate() ?: throw IllegalArgumentException(),
            entity.points,
            entity.comments,
            entity.totalRating ?: throw IllegalArgumentException(),
            entity.ratingCount ?: throw IllegalArgumentException(),
        )

    fun map(route: Route): RouteEntity =
        RouteEntity().apply {
            id = route.id
            routeName = route.routeName
            routeDescription = route.routeDescription
            points = route.points
            comments = route.comments
            createdAt = Timestamp(route.createdAt)
            totalRating = route.totalRating
            ratingCount = route.ratingCount
        }
}