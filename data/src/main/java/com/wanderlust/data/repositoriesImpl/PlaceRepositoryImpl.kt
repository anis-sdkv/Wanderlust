package com.wanderlust.data.repositoriesImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.wanderlust.data.entities.PlaceEntity
import com.wanderlust.data.mappers.PlaceMapper
import com.wanderlust.domain.model.Place
import com.wanderlust.domain.repositories.PlaceRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(private val db: FirebaseFirestore, private val mapper: PlaceMapper) :
    PlaceRepository {
    override suspend fun getByIdArray(ids: List<String>): List<Place> {
        if (ids.isEmpty()) return listOf()

        val query = db.collection(PLACES_COLLECTION)
            .whereIn("id", ids)
            .get()
            .await()

        val result = mutableListOf<Place>()
        query.documents.forEach {
            val entity = it.toObject(PlaceEntity::class.java)
            entity?.let { route -> result.add(mapper.map(route)) }
        }
        return result
    }

    override suspend fun getById(id: String): Place? {
        val document = db.collection(PLACES_COLLECTION)
            .document(id)
            .get()
            .await()

        return if (document.exists()) {
            val entity = document.toObject(PlaceEntity::class.java)
            entity?.let { mapper.map(it) }
        } else null
    }

    companion object {
        const val PLACES_COLLECTION = "places"
    }
}