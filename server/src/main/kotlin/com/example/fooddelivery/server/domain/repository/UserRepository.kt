package com.example.fooddelivery.server.domain.repository

import com.example.fooddelivery.server.domain.model.User

interface UserRepository {
    fun findByFirebaseUid(firebaseUid: String): User?

    fun syncFirebaseUser(
        firebaseUid: String,
        email: String?,
        displayName: String?,
        phone: String?,
    ): User
}
