package com.wanderlust.domain.repositories

import com.wanderlust.domain.model.Route

interface RouteRepository {
    suspend fun getByIdArray(ids: List<String>): List<Route>
    suspend fun getById(id: String): Route?
}