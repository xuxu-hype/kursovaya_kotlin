package com.example.fooddelivery.server.routes.dto

import com.example.fooddelivery.server.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val firebaseUid: String,
    val email: String?,
    val displayName: String?,
    val phone: String?,
    val role: String,
    val createdAt: String,
)

@Serializable
data class SyncUserRequest(
    val displayName: String? = null,
    val phone: String? = null,
)

fun User.toDto(): UserDto =
    UserDto(
        id = id.toString(),
        firebaseUid = firebaseUid,
        email = email,
        displayName = displayName,
        phone = phone,
        role = role,
        createdAt = createdAt.toString(),
    )
