package com.wanderlust.data.mappers

import com.google.firebase.Timestamp
import com.wanderlust.data.entities.PlaceEntity
import com.wanderlust.domain.model.Place

fun PlaceEntity.toDomain(id: String): Place =
    Place(
        this.authorId ?: throw IllegalArgumentException(),
        this.authorName ?: throw IllegalArgumentException(),
        this.lat ?: throw IllegalArgumentException(),
        this.lon ?: throw IllegalArgumentException(),
        this.placeName ?: throw IllegalArgumentException(),
        this.placeDescription ?: throw IllegalArgumentException(),
        this.city ?: throw IllegalArgumentException(),
        this.country ?: throw IllegalArgumentException(),
        this.comments.map { it.toComment() },
        this.createdAt?.toDate() ?: throw IllegalArgumentException(),
        this.totalRating ?: throw IllegalArgumentException(),
        this.ratingCount ?: throw IllegalArgumentException(),
        this.imagesUrl,
        this.tags,
        id
    )

fun Place.toEntity(): PlaceEntity =
    PlaceEntity().apply {
        authorId = this@toEntity.authorId
        authorName = this@toEntity.authorName
        lat = this@toEntity.lat
        lon = this@toEntity.lon
        placeName = this@toEntity.placeName
        placeDescription = this@toEntity.placeDescription
        city = this@toEntity.city
        country = this@toEntity.country
        comments = this@toEntity.comments.map { it.toEntity() }
        createdAt = Timestamp(this@toEntity.createdAt)
        totalRating = this@toEntity.totalRating
        ratingCount = this@toEntity.ratingCount
        imagesUrl = this@toEntity.imagesUrl
        tags = this@toEntity.tags
    }
