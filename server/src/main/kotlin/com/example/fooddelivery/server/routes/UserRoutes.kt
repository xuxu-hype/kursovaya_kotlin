package com.example.fooddelivery.server.routes

import com.example.fooddelivery.server.auth.FirebasePrincipal
import com.example.fooddelivery.server.domain.repository.UserRepository
import com.example.fooddelivery.server.domain.usecase.SyncUserUseCase
import com.example.fooddelivery.server.routes.dto.SyncUserRequest
import com.example.fooddelivery.server.routes.dto.toDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.registerUserRoutes(
    syncUser: SyncUserUseCase,
    userRepository: UserRepository,
) {
    post("/me/sync") {
        val principal = call.principal<FirebasePrincipal>()
            ?: return@post call.respondError(HttpStatusCode.Unauthorized, "UNAUTHORIZED", "Firebase token is required.")
        val request = call.receiveNullable<SyncUserRequest>()

        val user = syncUser(
            firebaseUid = principal.uid,
            email = principal.email,
            displayName = request?.displayName ?: principal.name,
            phone = request?.phone,
        )

        call.respond(user.toDto())
    }

    get("/me") {
        val principal = call.principal<FirebasePrincipal>()
            ?: return@get call.respondError(HttpStatusCode.Unauthorized, "UNAUTHORIZED", "Firebase token is required.")

        val user = userRepository.findByFirebaseUid(principal.uid)
            ?: return@get call.respondError(HttpStatusCode.NotFound, "NOT_FOUND", "User has not been synced yet.")

        call.respond(user.toDto())
    }
}
