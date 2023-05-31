package com.wanderlust.domain.model

interface RegisterResult

data class SuccessRegister(val user: User) : RegisterResult

data class FailRegister(val errorMessage: String? = null) : RegisterResult