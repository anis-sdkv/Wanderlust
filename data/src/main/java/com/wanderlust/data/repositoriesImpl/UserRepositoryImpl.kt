package com.wanderlust.data.repositoriesImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.model.User
import com.wanderlust.domain.repositories.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val db: FirebaseFirestore) : UserRepository {

    override suspend fun createUser(id: String, username: String): User {
        return try {
            val user = User(id, username)
            db.collection(USERS_COLLECTION)
                .add(user)
                .await()
            user
        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }

    override fun getUserByUserName(name: String): User {
        return User(
            "1",
            "Ivan",
            "Kazan",
            "Russia",
            "12345",
            listOf(
                Route(
                    "Route Name",
                    "Route Description",
                    emptyList()
                )
            ),
            listOf(
                User(
                    "1",
                    "Ivan",
                    "Kazan",
                    "Russia",
                    "12345",
                    listOf(
                        Route(
                            "Route Name",
                            "Route Description",
                            emptyList()
                        )
                    ),
                    listOf(),
                    listOf()
                )
            ),
            listOf(
                User(
                    "1",
                    "Ivan",
                    "Kazan",
                    "Russia",
                    "12345",
                    listOf(
                        Route(
                            "Route Name",
                            "Route Description",
                            emptyList()
                        )
                    ),
                    listOf(),
                    listOf()
                )
            )
        )
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}
