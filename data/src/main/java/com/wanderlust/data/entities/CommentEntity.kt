package com.wanderlust.data.entities

import com.google.firebase.Timestamp

class CommentEntity {
    var authorId: String? = null
    var authorNickname: String? = null
    var text: String? = null
    var score: Int? = null
    var createdAt: Timestamp? = null
}