package com.wanderlust.data.repositoriesImpl

import com.wanderlust.domain.model.User
import com.wanderlust.domain.repositories.UserRepository

class UserRepositoryImpl : UserRepository {
    override fun getUserByUserName(name: String): User {
        TODO("Not yet implemented")
    }
}