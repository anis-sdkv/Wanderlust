package com.wanderlust.data.repositoriesImpl

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.wanderlust.data.entities.RouteEntity
import com.wanderlust.data.mappers.RouteMapper
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.repositories.RouteRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(private val db: FirebaseFirestore, private val mapper: RouteMapper) :
    RouteRepository {

    override suspend fun create(userId: String, route: Route) {
        val entity = mapper.map(route)

        db.runTransaction { transaction ->
            val routeDoc = db.collection(ROUTES_COLLECTION).document()
            val userDoc = db.collection(UserRepositoryImpl.USERS_COLLECTION).document(userId)

            transaction.set(routeDoc, entity)
            transaction.update(userDoc, "routes", FieldValue.arrayUnion(routeDoc.id))
        }.await()
    }

    override suspend fun getByFilters(regex: Regex, searchByName: Boolean, tags: List<String>): List<Route> {
        val query = db.collection(ROUTES_COLLECTION)
            .get()
            .await()

        val result = mutableListOf<Route>()
        query.documents.forEach {
            val entity = it.toObject(RouteEntity::class.java)
            entity?.let { route ->
                val model = mapper.map(route)
                if (model.tags.containsAll(tags)) {
                    if (searchByName)
                        if (regex.matches(model.routeName))
                            result.add(model)
                        else
                            if (regex.matches(model.authorName!!))
                                result.add(model)

                }
            }
        }
        return result
    }

    override suspend fun getAll(): List<Route> {
        val query = db.collection(ROUTES_COLLECTION)
            .get()
            .await()

        val result = mutableListOf<Route>()
        query.documents.forEach {
            val entity = it.toObject(RouteEntity::class.java)
            entity?.let { route -> result.add(mapper.map(route)) }
        }
        return result
    }

    override suspend fun getByIdArray(ids: List<String>): List<Route> {
        if (ids.isEmpty()) return listOf()

        val query = db.collection(ROUTES_COLLECTION)
            .whereIn(FieldPath.documentId(), ids)
            .get()
            .await()

        val result = mutableListOf<Route>()
        query.documents.forEach {
            val entity = it.toObject(RouteEntity::class.java)
            entity?.let { route -> result.add(mapper.map(route)) }
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