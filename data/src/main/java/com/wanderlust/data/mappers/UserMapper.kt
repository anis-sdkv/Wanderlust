package com.wanderlust.data.mappers

import com.google.firebase.Timestamp
import com.wanderlust.data.entities.UserEntity
import com.wanderlust.domain.model.UserProfile

fun UserEntity.toDomain(id: String): UserProfile =
    UserProfile(
        id,
        this.username ?: throw IllegalArgumentException(),
        this.createdAt?.toDate() ?: throw IllegalArgumentException(),
        this.city,
        this.country,
        this.description,
        this.routes,
        this.places,
        this.subscribers,
        this.subscriptions,
    )

fun UserProfile.toEntity(): UserEntity =
    UserEntity().apply {
        username = this@toEntity.username
        city = this@toEntity.city
        country = this@toEntity.country
        description = this@toEntity.description
        routes = this@toEntity.routes
        places = this@toEntity.places
        subscribers = this@toEntity.subscribers
        subscriptions = this@toEntity.subscriptions
        createdAt = Timestamp(this@toEntity.createdAt)
    }