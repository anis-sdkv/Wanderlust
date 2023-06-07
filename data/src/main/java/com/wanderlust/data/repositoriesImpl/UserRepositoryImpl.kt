package com.wanderlust.data.repositoriesImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.wanderlust.data.entities.UserEntity
import com.wanderlust.data.mappers.toDomain
import com.wanderlust.data.mappers.toEntity
import com.wanderlust.domain.model.UserProfile
import com.wanderlust.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRepository {

    override suspend fun createUser(id: String, username: String): Unit = withContext(Dispatchers.IO) {
        val user = UserProfile(id, username, Date()).toEntity()
        db.collection(USERS_COLLECTION)
            .document(id)
            .set(user)
            .await()
    }

    override suspend fun getById(id: String): UserProfile? = withContext(Dispatchers.IO) {
        val doc = db.collection(USERS_COLLECTION)
            .document(id)
            .get()
            .await()

        return@withContext if (doc.exists()) doc.toObject(UserEntity::class.java)?.toDomain(doc.id)
        else null
    }

    override suspend fun update(userProfile: UserProfile): Unit = withContext(Dispatchers.IO) {
        val entity = userProfile.toEntity()
        db.collection(USERS_COLLECTION)
            .document(userProfile.id)
            .set(entity)
            .await()
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}
