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
            .document(id)
            .set(user)
            .await()
    }

    override suspend fun getById(id: String): UserProfile? {
        val doc = db.collection(USERS_COLLECTION)
            .document(id)
            .get()
            .await()

        return if (doc.exists()) {
            val entity = doc.toObject(UserEntity::class.java) ?: return null
            mapper.map(entity, id)
        } else null
    }

    override suspend fun update(userProfile: UserProfile) {
        val entity = mapper.map(userProfile)
        db.collection(USERS_COLLECTION)
            .document(userProfile.id)
            .set(entity)
    }

    override fun getUserByUserName(name: String): UserProfile {
        TODO()
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}
