package com.example.fooddelivery.server.routes

import com.example.fooddelivery.server.auth.FirebasePrincipal
import com.example.fooddelivery.server.domain.repository.UserRepository
import com.example.fooddelivery.server.domain.usecase.CreateOrderInput
import com.example.fooddelivery.server.domain.usecase.CreateOrderItemInput
import com.example.fooddelivery.server.domain.usecase.CreateOrderUseCase
import com.example.fooddelivery.server.domain.usecase.GetMyOrdersUseCase
import com.example.fooddelivery.server.domain.usecase.GetOrderByIdUseCase
import com.example.fooddelivery.server.domain.usecase.UseCaseError
import com.example.fooddelivery.server.routes.dto.CreateOrderRequest
import com.example.fooddelivery.server.routes.dto.toDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.registerOrderRoutes(
    createOrder: CreateOrderUseCase,
    getMyOrders: GetMyOrdersUseCase,
    getOrderById: GetOrderByIdUseCase,
    userRepository: UserRepository,
) {
    post("/orders") {
        val currentUser = call.currentSyncedUser(userRepository)
            ?: return@post call.respondError(HttpStatusCode.NotFound, "USER_NOT_SYNCED", "Firebase user has not been synced yet.")
        val request = call.receive<CreateOrderRequest>()

        val restaurantId = parseUuid(request.restaurantId)
            ?: return@post call.respondError(HttpStatusCode.BadRequest, "VALIDATION_ERROR", "Restaurant id must be a valid UUID.")

        val items = request.items.map { item ->
            val menuItemId = parseUuid(item.menuItemId)
                ?: return@post call.respondError(HttpStatusCode.BadRequest, "VALIDATION_ERROR", "Menu item id must be a valid UUID.")
            CreateOrderItemInput(menuItemId = menuItemId, quantity = item.quantity)
        }

        try {
            val order = createOrder(
                CreateOrderInput(
                    userId = currentUser.id,
                    restaurantId = restaurantId,
                    deliveryAddress = request.deliveryAddress,
                    items = items,
                ),
            )
            call.respond(HttpStatusCode.Created, order.toDto())
        } catch (error: UseCaseError.Validation) {
            call.respondError(HttpStatusCode.BadRequest, "VALIDATION_ERROR", error.message ?: "Invalid order.")
        } catch (error: UseCaseError.NotFound) {
            call.respondError(HttpStatusCode.NotFound, "NOT_FOUND", error.message ?: "Not found.")
        }
    }

    get("/orders/my") {
        val currentUser = call.currentSyncedUser(userRepository)
            ?: return@get call.respondError(HttpStatusCode.NotFound, "USER_NOT_SYNCED", "Firebase user has not been synced yet.")

        call.respond(getMyOrders(currentUser.id).map { it.toDto() })
    }

    get("/orders/{id}") {
        val currentUser = call.currentSyncedUser(userRepository)
            ?: return@get call.respondError(HttpStatusCode.NotFound, "USER_NOT_SYNCED", "Firebase user has not been synced yet.")
        val orderId = parseUuid(call.parameters["id"])
            ?: return@get call.respondError(HttpStatusCode.BadRequest, "VALIDATION_ERROR", "Order id must be a valid UUID.")

        try {
            call.respond(getOrderById(orderId, currentUser.id).toDto())
        } catch (_: UseCaseError.NotFound) {
            call.respondError(HttpStatusCode.NotFound, "NOT_FOUND", "Order not found.")
        }
    }
}

private fun io.ktor.server.application.ApplicationCall.currentFirebasePrincipal(): FirebasePrincipal? =
    principal<FirebasePrincipal>()

private fun io.ktor.server.application.ApplicationCall.currentSyncedUser(
    userRepository: UserRepository,
) = currentFirebasePrincipal()?.let { userRepository.findByFirebaseUid(it.uid) }
