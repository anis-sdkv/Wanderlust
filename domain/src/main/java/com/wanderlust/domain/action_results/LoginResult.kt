package com.wanderlust.domain.action_results

sealed interface LoginResult {
    data class SuccessLogin(val userId: String) : LoginResult
    data class FailLogin(val errorMessage: String? = null) : LoginResult
}