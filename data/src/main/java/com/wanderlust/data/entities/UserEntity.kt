package com.wanderlust.data.entities

import com.google.firebase.Timestamp


class UserEntity {
    var username: String? = null
    var city: String? = null
    var country: String? = null
    var description: String? = null
    var routes: List<String> = listOf()
    var places: List<String> = listOf()
    var subscribers: List<String> = listOf()
    var subscriptions: List<String> = listOf()
    var imagesUrl: List<String> = listOf()
    var createdAt: Timestamp? = null
}