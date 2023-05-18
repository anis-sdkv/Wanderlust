package com.wanderlust.data.repositoriesImpl

import com.wanderlust.domain.model.Route
import com.wanderlust.domain.model.User
import com.wanderlust.domain.repositories.UserRepository

class UserRepositoryImpl : UserRepository {
    override fun getUserByUserName(name: String): User {
        return User(
            "Ivan",
            "Kazan",
            "Russia",
            "12345",
            listOf(Route()),
            listOf(
                User(
                    "Ivan",
                    "Kazan",
                    "Russia",
                    "12345",
                    listOf(Route()),
                    listOf(),
                    listOf()
                )
            ),
            listOf(
                User(
                    "Ivan",
                    "Kazan",
                    "Russia",
                    "12345",
                    listOf(Route()),
                    listOf(),
                    listOf()
                )
            )
        )
    }
}