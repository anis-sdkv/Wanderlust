package com.wanderlust.domain.model

class User(
    val userName: String,
    val userCity: String,
    val userCountry: String,
    val userDescription: String,
    val userRoutes: List<Route>,
    val userSubscribers: List<User>,
    val userSubscriptions: List<User>
)