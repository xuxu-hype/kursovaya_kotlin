package com.example.fooddelivery.server.domain.model

import java.time.Instant
import java.util.UUID

data class User(
    val id: UUID,
    val firebaseUid: String,
    val email: String?,
    val displayName: String?,
    val phone: String?,
    val role: String,
    val createdAt: Instant,
)
