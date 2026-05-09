package com.example.fooddelivery.server.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
)

fun Routing.registerHealthRoutes() {
    get("/health") {
        call.respond(HealthResponse(status = "OK"))
    }
}
