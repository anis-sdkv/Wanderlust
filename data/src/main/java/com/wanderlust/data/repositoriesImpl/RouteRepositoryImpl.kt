package com.wanderlust.data.repositoriesImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.wanderlust.data.entities.RouteEntity
import com.wanderlust.data.mappers.RouteMapper
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.repositories.RouteRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(private val db: FirebaseFirestore, private val mapper: RouteMapper) :
    RouteRepository {
    override suspend fun getByIdArray(ids: List<String>): List<Route> {
        if (ids.isEmpty()) return listOf()

        val query = db.collection(ROUTES_COLLECTION)
            .whereIn("id", ids)
            .get()
            .await()

        val result = mutableListOf<Route>()
        query.documents.forEach {
            val route = it.toObject(RouteEntity::class.java)
            route?.let { it1 -> result.add(mapper.map(route)) }
        }
        return result
    }

    override suspend fun getById(id: String): Route? {
        val document = db.collection(ROUTES_COLLECTION)
            .document(id)
            .get()
            .await()

        return if (document.exists()) {
            val entity = document.toObject(RouteEntity::class.java)
            entity?.let { mapper.map(it) }
        } else null
    }

    companion object {
        const val ROUTES_COLLECTION = "routes"

    }
}