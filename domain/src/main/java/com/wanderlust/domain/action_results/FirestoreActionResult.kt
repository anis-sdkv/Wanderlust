package com.wanderlust.domain.action_results

sealed interface FirestoreActionResult{
    object SuccessResult : FirestoreActionResult
    data class FailResult(val message: String?) : FirestoreActionResult
}