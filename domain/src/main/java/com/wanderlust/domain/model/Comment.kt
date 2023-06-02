package com.wanderlust.domain.model

import java.util.Date

data class Comment(
    val authorId: String,
    val authorNickname: String,
    val score: Int,
    val createdAt: Date,
    val text: String? = null,
)

