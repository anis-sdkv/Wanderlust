package com.wanderlust.domain.model

data class Route (
    val routeName: String,
    val routeDescription: String,
    val listOfPlaces: List<Place>
)