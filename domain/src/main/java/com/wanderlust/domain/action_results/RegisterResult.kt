package com.wanderlust.domain.action_results

sealed interface RegisterResult {
    data class SuccessRegister(val id: String) : RegisterResult
    data class FailRegister(val errorMessage: String? = null) : RegisterResult
}
