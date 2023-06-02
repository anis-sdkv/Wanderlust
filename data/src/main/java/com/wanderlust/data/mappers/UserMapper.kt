package com.wanderlust.data.mappers

import com.google.firebase.Timestamp
import com.wanderlust.data.entities.UserEntity
import com.wanderlust.domain.model.UserProfile
import javax.inject.Inject

class UserMapper @Inject constructor() {
    fun map(entity: UserEntity, id: String): UserProfile =
        UserProfile(
            id,
            entity.username ?: throw IllegalArgumentException(),
            entity.createdAt?.toDate() ?: throw IllegalArgumentException(),
            entity.city,
            entity.country,
            entity.description,
            entity.routes,
            entity.places,
            entity.subscribers,
            entity.subscriptions,
        )

    fun map(userProfile: UserProfile): UserEntity =
        UserEntity().apply {
            username = userProfile.username
            city = userProfile.city
            country = userProfile.country
            description = userProfile.description
            routes = userProfile.routes
            places = userProfile.places
            subscribers = userProfile.subscribers
            subscriptions = userProfile.subscriptions
            createdAt = Timestamp(userProfile.createdAt)
        }
}