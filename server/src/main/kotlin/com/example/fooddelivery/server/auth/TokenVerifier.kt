package com.example.fooddelivery.server.auth

interface TokenVerifier {
    fun verify(token: String): FirebasePrincipal?
}
