package com.example.fooddelivery.server.domain.usecase

import com.example.fooddelivery.server.domain.model.User
import com.example.fooddelivery.server.domain.repository.UserRepository

class SyncUserUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        firebaseUid: String,
        email: String?,
        displayName: String?,
        phone: String?,
    ): User =
        userRepository.syncFirebaseUser(
            firebaseUid = firebaseUid,
            email = email,
            displayName = displayName,
            phone = phone,
        )
}
