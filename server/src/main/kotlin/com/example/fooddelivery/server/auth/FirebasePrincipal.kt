package com.example.fooddelivery.server.auth

/** Authenticated Firebase user; used as Ktor auth principal for the `firebase` bearer provider. */
data class FirebasePrincipal(
    val uid: String,
    val email: String?,
)
