package com.example.kursovayakotlin.data.remote.dto

data class UserDto(
    val id: String,
    val firebaseUid: String,
    val email: String?,
    val displayName: String?,
    val phone: String?,
    val role: String,
    val createdAt: String,
)

data class SyncUserRequestDto(
    val displayName: String? = null,
    val phone: String? = null,
)
