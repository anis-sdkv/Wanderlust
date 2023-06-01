package com.wanderlust.data.repositoriesImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.wanderlust.data.entities.UserEntity
import com.wanderlust.data.mappers.UserMapper
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.UserRepository
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val mapper: UserMapper
) : UserRepository {

    override suspend fun createUser(id: String, username: String) {
        val user = mapper.map(UserProfile(id, username, Date()))
        db.collection(USERS_COLLECTION)
            .add(user)
            .await()
    }

    override suspend fun getById(id: String): UserProfile? {
        val query = db.collection(USERS_COLLECTION)
            .whereEqualTo("id", id)
            .get()
            .await()

        return if (!query.isEmpty) {
            val entity = query.documents.first()
                .toObject(UserEntity::class.java) ?: return null
            mapper.map(entity)
        } else null
    }

    override fun getUserByUserName(name: String): UserProfile {
        TODO()
//        return UserProfile(
//            "1",
//            "Ivan",
//            "Kazan",
//            "Russia",
//            "12345",
//            listOf(
//                Route(
//                    "Route Name",
//                    "Route Description",
//                    emptyList()
//                )
//            ),
//            listOf(
//                UserProfile(
//                    "1",
//                    "Ivan",
//                    "Kazan",
//                    "Russia",
//                    "12345",
//                    listOf(
//                        Route(
//                            "Route Name",
//                            "Route Description",
//                            emptyList()
//                        )
//                    ),
//                    listOf(),
//                    listOf()
//                )
//            ),
//            listOf(
//                UserProfile(
//                    "1",
//                    "Ivan",
//                    "Kazan",
//                    "Russia",
//                    "12345",
//                    listOf(
//                        Route(
//                            "Route Name",
//                            "Route Description",
//                            emptyList()
//                        )
//                    ),
//                    listOf(),
//                    listOf()
//                )
//            )
//        )
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}
