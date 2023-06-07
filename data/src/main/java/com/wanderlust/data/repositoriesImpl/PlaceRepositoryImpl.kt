package com.wanderlust.data.repositoriesImpl

import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.wanderlust.data.entities.PlaceEntity
import com.wanderlust.data.mappers.toDomain
import com.wanderlust.data.mappers.toEntity
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.model.Place
import com.wanderlust.domain.repositories.PlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(private val db: FirebaseFirestore) :
    PlaceRepository {
    override suspend fun create(userId: String, place: Place): Unit = withContext(Dispatchers.IO) {
        val entity = place.toEntity()

        db.runTransaction { transaction ->
            val placeDoc = db.collection(PLACES_COLLECTION).document()
            val userDoc = db.collection(UserRepositoryImpl.USERS_COLLECTION).document(userId)

            transaction.set(placeDoc, entity)
            transaction.update(userDoc, "places", FieldValue.arrayUnion(placeDoc.id))
        }.await()
    }

    override suspend fun addComment(placeId: String, comment: Comment) {
        val doc = db.collection(PLACES_COLLECTION).document(placeId)
        val fieldsToUpdate = hashMapOf<String, Any>(
            "comments" to FieldValue.arrayUnion(comment),
            "ratingCount" to FieldValue.increment(1),
            "totalRating" to FieldValue.increment(comment.score.toLong())
        )

        doc.update(fieldsToUpdate)
            .await()
    }

    override suspend fun getAll(): List<Place> = withContext(Dispatchers.IO) {
        val query = db.collection(PLACES_COLLECTION)
            .get()
            .await()

        val result = mutableListOf<Place>()
        query.documents.forEach {
            val entity = it.toObject(PlaceEntity::class.java)
            entity?.let { place -> result.add(place.toDomain(it.id)) }
        }
        result
    }

    override suspend fun getByIdArray(ids: List<String>): List<Place> = withContext(Dispatchers.IO) {
        if (ids.isEmpty()) return@withContext listOf()

        val query = db.collection(PLACES_COLLECTION)
            .whereIn("id", ids)
            .get()
            .await()

        val result = mutableListOf<Place>()
        query.documents.forEach {
            val entity = it.toObject(PlaceEntity::class.java)
            entity?.let { place -> result.add(place.toDomain(it.id)) }
        }
        result
    }

    override suspend fun getById(id: String): Place? = withContext(Dispatchers.IO) {
        val document = db.collection(PLACES_COLLECTION)
            .document(id)
            .get()
            .await()

        if (document.exists()) {
            val entity = document.toObject(PlaceEntity::class.java)
            entity?.toDomain(document.id)
        } else null
    }

    companion object {
        const val PLACES_COLLECTION = "places"
    }
}