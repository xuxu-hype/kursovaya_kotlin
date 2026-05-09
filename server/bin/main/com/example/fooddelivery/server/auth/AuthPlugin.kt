package com.example.fooddelivery.server.auth

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer

/**
 * Registers Ktor [Authentication] with bearer scheme `"firebase"`.
 * Protected routes can use `authenticate("firebase") { ... }` and read `FirebasePrincipal` via `call.principal<FirebasePrincipal>()`.
 */
fun Application.installFirebaseAuth(firebaseAuthService: FirebaseAuthService) {
    install(Authentication) {
        bearer("firebase") {
            realm = "FoodDelivery API"
            authenticate { credential ->
                firebaseAuthService.verifyIdToken(credential.token)
            }
        }
    }
}
