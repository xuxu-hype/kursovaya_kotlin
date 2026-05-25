package com.example.fooddelivery.server.auth

class FakeTokenVerifier(
    private val principalsByToken: Map<String, FirebasePrincipal>,
) : TokenVerifier {
    override fun verify(token: String): FirebasePrincipal? = principalsByToken[token]
}
