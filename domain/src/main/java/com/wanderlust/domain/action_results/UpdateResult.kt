package com.wanderlust.domain.action_results

sealed interface UpdateResult{
    object SuccessResult : UpdateResult
    data class FailResult(val message: String?) : UpdateResult
}