package com.wanderlust.data.mappers

import com.google.firebase.Timestamp
import com.wanderlust.data.entities.PlaceEntity
import com.wanderlust.domain.model.Place
import javax.inject.Inject

class PlaceMapper @Inject constructor() {
    fun map(entity: PlaceEntity): Place =
        Place(
            entity.id ?: throw IllegalArgumentException(),
            entity.lat ?: throw IllegalArgumentException(),
            entity.lon ?: throw IllegalArgumentException(),
            entity.placeName ?: throw IllegalArgumentException(),
            entity.placeDescription ?: throw IllegalArgumentException(),
            entity.comments,
            entity.createdAt?.toDate() ?: throw IllegalArgumentException(),
            entity.totalRating ?: throw IllegalArgumentException(),
            entity.ratingCount ?: throw IllegalArgumentException(),
            entity.imagesUrl
        )

    fun map(place: Place): PlaceEntity =
        PlaceEntity().apply {
            id = place.id
            lat = place.lat
            lon = place.lon
            placeName = place.placeName
            placeDescription = place.placeDescription
            comments = place.comments
            createdAt = Timestamp(place.createdAt)
            totalRating = place.totalRating
            ratingCount = place.ratingCount
            imagesUrl = place.imagesUrl
        }
}