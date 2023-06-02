package com.wanderlust.data.mappers

import com.google.firebase.Timestamp
import com.wanderlust.data.entities.PlaceEntity
import com.wanderlust.domain.model.Place
import javax.inject.Inject

class PlaceMapper @Inject constructor() {
    fun map(entity: PlaceEntity): Place =
        Place(
            entity.lat ?: throw IllegalArgumentException(),
            entity.lon ?: throw IllegalArgumentException(),
            entity.placeName ?: throw IllegalArgumentException(),
            entity.placeDescription ?: throw IllegalArgumentException(),
            entity.city ?: throw IllegalArgumentException(),
            entity.country ?: throw IllegalArgumentException(),
            entity.comments.map { it.toComment() },
            entity.createdAt?.toDate() ?: throw IllegalArgumentException(),
            entity.totalRating ?: throw IllegalArgumentException(),
            entity.ratingCount ?: throw IllegalArgumentException(),
            entity.imagesUrl,
            entity.tags
        )

    fun map(place: Place): PlaceEntity =
        PlaceEntity().apply {
            lat = place.lat
            lon = place.lon
            placeName = place.placeName
            placeDescription = place.placeDescription
            city = place.city
            country = place.country
            comments = place.comments.map { it.toEntity() }
            createdAt = Timestamp(place.createdAt)
            totalRating = place.totalRating
            ratingCount = place.ratingCount
            imagesUrl = place.imagesUrl
            tags = place.tags
        }
}