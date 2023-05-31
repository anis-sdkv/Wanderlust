package com.wanderlust.domain.model

data class User(
    val id: String,
    val userName: String,
    val userCity: String? = null,
    val userCountry: String? = null,
    val userDescription: String? = null,
    val userRoutes: List<Route> = listOf(),
    val userSubscribers: List<User> = listOf(),
    val userSubscriptions: List<User> = listOf()
)