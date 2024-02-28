package com.wanderlust.domain.model


import java.util.Date

data class UserProfile(
    val id: String,
    val username: String,
    val createdAt: Date,
    val city: String? = null,
    val country: String? = null,
    val description: String? = null,
    val routes: List<String> = listOf(),
    val places: List<String> = listOf(),
    val subscribers: List<String> = listOf(),
    val subscriptions: List<String> = listOf(),
    val imagesUrl: List<String> = listOf(),
)