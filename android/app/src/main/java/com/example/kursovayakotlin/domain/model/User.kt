package com.example.kursovayakotlin.domain.model

data class User(
    val id: String,
    val firebaseUid: String,
    val email: String?,
    val displayName: String?,
    val phone: String?,
    val role: String,
    val createdAt: String,
)
