package com.wanderlust.domain.usecases

import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.repositories.RouteRepository

class AddRouteCommentUseCase(private val routeRepository: RouteRepository) {
    suspend operator fun invoke(routeId: String, comment: Comment): FirestoreActionResult {
        return try {
            routeRepository.addComment(routeId, comment)
            FirestoreActionResult.SuccessResult
        } catch (e: Exception) {
            FirestoreActionResult.FailResult(e.message)
        }
    }
}