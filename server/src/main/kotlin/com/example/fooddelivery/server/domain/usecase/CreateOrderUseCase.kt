package com.example.fooddelivery.server.domain.usecase

import com.example.fooddelivery.server.domain.model.Order
import com.example.fooddelivery.server.domain.model.OrderStatus
import com.example.fooddelivery.server.domain.repository.NewOrderItem
import com.example.fooddelivery.server.domain.repository.OrderRepository
import com.example.fooddelivery.server.domain.repository.RestaurantRepository
import java.util.UUID

data class CreateOrderInput(
    val userId: UUID,
    val restaurantId: UUID,
    val deliveryAddress: String,
    val items: List<CreateOrderItemInput>,
)

data class CreateOrderItemInput(
    val menuItemId: UUID,
    val quantity: Int,
)

class CreateOrderUseCase(
    private val restaurantRepository: RestaurantRepository,
    private val orderRepository: OrderRepository,
) {
    operator fun invoke(input: CreateOrderInput): Order {
        if (input.deliveryAddress.isBlank()) {
            throw UseCaseError.Validation("Delivery address must not be blank.")
        }
        if (input.items.isEmpty()) {
            throw UseCaseError.Validation("Order must contain at least one item.")
        }

        restaurantRepository.findById(input.restaurantId)
            ?: throw UseCaseError.NotFound("Restaurant not found.")

        val orderItems = input.items.map { requestItem ->
            if (requestItem.quantity <= 0) {
                throw UseCaseError.Validation("Quantity must be greater than zero.")
            }

            val menuItem = restaurantRepository.findMenuItemById(requestItem.menuItemId)
                ?: throw UseCaseError.NotFound("Menu item not found.")

            if (menuItem.restaurantId != input.restaurantId) {
                throw UseCaseError.Validation("Menu item does not belong to this restaurant.")
            }
            if (!menuItem.isAvailable) {
                throw UseCaseError.Validation("Menu item is not available.")
            }

            NewOrderItem(
                menuItemId = menuItem.id,
                nameSnapshot = menuItem.name,
                priceCents = menuItem.priceCents,
                quantity = requestItem.quantity,
            )
        }

        val totalCents = orderItems.sumOf { it.priceCents * it.quantity }

        return orderRepository.create(
            userId = input.userId,
            restaurantId = input.restaurantId,
            status = OrderStatus.CREATED,
            totalCents = totalCents,
            deliveryAddress = input.deliveryAddress.trim(),
            items = orderItems,
        )
    }
}
