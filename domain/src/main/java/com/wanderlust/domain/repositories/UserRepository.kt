package com.wanderlust.domain.repositories

import com.wanderlust.domain.model.Route
import com.wanderlust.domain.model.User

interface UserRepository {
    fun getUserByUserName(name: String) : User
}