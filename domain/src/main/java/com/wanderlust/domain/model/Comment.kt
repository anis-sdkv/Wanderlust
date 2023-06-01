package com.wanderlust.domain.model

import java.util.Date

data class Comment(
    val authorId: String,
    val authorNickname: String,
    val text: String,
    val score: Int,
    val createdAt: Date
)

