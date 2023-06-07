package com.wanderlust.data.mappers

import com.google.firebase.Timestamp
import com.wanderlust.data.entities.CommentEntity
import com.wanderlust.data.entities.RoutePointEntity
import com.wanderlust.domain.model.Comment
import com.wanderlust.domain.model.RoutePoint

fun Comment.toEntity(): CommentEntity {
    val comment = this
    return CommentEntity().apply {
        authorId = comment.authorId
        authorNickname = comment.authorNickname
        score = comment.score
        createdAt = Timestamp(comment.createdAt)
        comment.text
    }
}

fun CommentEntity.toComment() = Comment(
    authorId ?: throw IllegalArgumentException(),
    authorNickname ?: throw IllegalArgumentException(),
    score ?: throw IllegalArgumentException(),
    createdAt?.toDate() ?: throw IllegalArgumentException(),
    text
)

fun RoutePoint.toEntity(): RoutePointEntity {
    val point = this
    return RoutePointEntity().apply {
        lat = point.lat
        lon = point.lon
        name = point.name
        description = point.description
        imagesUrl = point.imagesUrl
    }
}

fun RoutePointEntity.toRoutePoint() = RoutePoint(
    lat ?: throw IllegalArgumentException(),
    lon ?: throw IllegalArgumentException(),
    name ?: throw IllegalArgumentException(),
    description ?: throw IllegalArgumentException(),
    imagesUrl,
)


