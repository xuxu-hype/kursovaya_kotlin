package com.example.fooddelivery.server.plugins

import com.example.fooddelivery.server.routes.registerHealthRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        registerHealthRoutes()
    }
}
