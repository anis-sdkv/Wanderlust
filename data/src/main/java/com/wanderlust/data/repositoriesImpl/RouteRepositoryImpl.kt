package com.wanderlust.data.repositoriesImpl

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.wanderlust.data.entities.RouteEntity
import com.wanderlust.data.mappers.toDomain
import com.wanderlust.data.mappers.toEntity
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.model.Route
import com.wanderlust.domain.repositories.RouteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(private val db: FirebaseFirestore) :
    RouteRepository {

    override suspend fun create(userId: String, route: Route): Unit = withContext(Dispatchers.IO) {
        val entity = route.toEntity()

        db.runTransaction { transaction ->
            val routeDoc = db.collection(ROUTES_COLLECTION).document()
            val userDoc = db.collection(UserRepositoryImpl.USERS_COLLECTION).document(userId)

            transaction.set(routeDoc, entity)
            transaction.update(userDoc, "routes", FieldValue.arrayUnion(routeDoc.id))
        }.await()
    }

    override suspend fun addComment(routeId: String, comment: Comment) {
        val doc = db.collection(ROUTES_COLLECTION).document(routeId)
        val fieldsToUpdate = hashMapOf<String, Any>(
            "comments" to FieldValue.arrayUnion(comment),
            "ratingCount" to FieldValue.increment(1),
            "totalRating" to FieldValue.increment(comment.score.toLong())
        )
        doc
            .update(fieldsToUpdate)
            .await()
    }

    override suspend fun getAll(): List<Route> = withContext(Dispatchers.IO) {
        val query = db.collection(ROUTES_COLLECTION)
            .get()
            .await()

        val result = mutableListOf<Route>()
        query.documents.forEach {
            val entity = it.toObject(RouteEntity::class.java)
            entity?.let { route -> result.add(route.toDomain(it.id)) }
        }
        result
    }

    override suspend fun getByIdArray(ids: List<String>): List<Route> = withContext(Dispatchers.IO) {
        if (ids.isEmpty()) return@withContext listOf()

        val query = db.collection(ROUTES_COLLECTION)
            .whereIn(FieldPath.documentId(), ids)
            .get()
            .await()

        val result = mutableListOf<Route>()
        query.documents.forEach {
            val entity = it.toObject(RouteEntity::class.java)
            entity?.let { route -> result.add(route.toDomain(it.id)) }
        }
        result
    }

    override suspend fun getById(id: String): Route? = withContext(Dispatchers.IO) {
        val document = db.collection(ROUTES_COLLECTION)
            .document(id)
            .get()
            .await()

        if (document.exists()) {
            val entity = document.toObject(RouteEntity::class.java)
            entity?.toDomain(document.id)
        } else null
    }

    companion object {
        const val ROUTES_COLLECTION = "routes"
    }
}
//    override suspend fun getByFilters(regex: Regex, searchByName: Boolean, tags: List<String>): List<Route> =
//        withContext(Dispatchers.IO) {
//            val query = db.collection(ROUTES_COLLECTION)
//                .get()
//                .await()
//
//            val result = mutableListOf<Route>()
//            query.documents.forEach {
//                val entity = it.toObject(RouteEntity::class.java)
//                entity?.let { route ->
//                    val model = route.toDomain(it.id)
//                    if (model.tags.containsAll(tags)) {
//                        if (searchByName)
//                            if (regex.matches(model.routeName))
//                                result.add(model)
//                            else
//                                if (regex.matches(model.authorName!!))
//                                    result.add(model)
//
//                    }
//                }
//            }
//            result
//    }
