package com.wanderlust.domain.usecases

import com.wanderlust.domain.action_results.FirestoreActionResult
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.repositories.PlaceRepository

class AddPlaceCommentUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(placeId: String, comment: Comment): FirestoreActionResult {
        return try {
            placeRepository.addComment(placeId, comment)
            FirestoreActionResult.SuccessResult
        } catch (e: Exception) {
            FirestoreActionResult.FailResult(e.message)
        }
    }
}