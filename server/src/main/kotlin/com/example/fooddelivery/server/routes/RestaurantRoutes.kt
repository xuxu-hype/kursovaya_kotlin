package com.example.fooddelivery.server.routes

import com.example.fooddelivery.server.domain.usecase.GetMenuUseCase
import com.example.fooddelivery.server.domain.usecase.GetRestaurantByIdUseCase
import com.example.fooddelivery.server.domain.usecase.GetRestaurantsUseCase
import com.example.fooddelivery.server.domain.usecase.UseCaseError
import com.example.fooddelivery.server.routes.dto.toDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.registerRestaurantRoutes(
    getRestaurants: GetRestaurantsUseCase,
    getRestaurantById: GetRestaurantByIdUseCase,
    getMenu: GetMenuUseCase,
) {
    get("/restaurants") {
        call.respond(getRestaurants().map { it.toDto() })
    }

    get("/restaurants/{id}") {
        val id = parseUuid(call.parameters["id"])
            ?: return@get call.respondError(
                HttpStatusCode.BadRequest,
                "VALIDATION_ERROR",
                "Restaurant id must be a valid UUID.",
            )

        try {
            call.respond(getRestaurantById(id).toDto())
        } catch (_: UseCaseError.NotFound) {
            call.respondError(HttpStatusCode.NotFound, "NOT_FOUND", "Restaurant not found.")
        }
    }

    get("/restaurants/{id}/menu") {
        val id = parseUuid(call.parameters["id"])
            ?: return@get call.respondError(
                HttpStatusCode.BadRequest,
                "VALIDATION_ERROR",
                "Restaurant id must be a valid UUID.",
            )

        try {
            call.respond(getMenu(id).map { it.toDto() })
        } catch (_: UseCaseError.NotFound) {
            call.respondError(HttpStatusCode.NotFound, "NOT_FOUND", "Restaurant not found.")
        }
    }
}
