package com.example.kursovayakotlin.data.repository

import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.data.local.dao.CartDao
import com.example.kursovayakotlin.data.local.dao.OrderDao
import com.example.kursovayakotlin.data.mapper.toDomain
import com.example.kursovayakotlin.data.mapper.toEntity
import com.example.kursovayakotlin.data.remote.api.FoodApi
import com.example.kursovayakotlin.data.remote.dto.CreateOrderItemRequestDto
import com.example.kursovayakotlin.data.remote.dto.CreateOrderRequestDto
import com.example.kursovayakotlin.domain.model.Order
import com.example.kursovayakotlin.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val foodApi: FoodApi,
    private val cartDao: CartDao,
    private val orderDao: OrderDao,
) : OrderRepository {
    override suspend fun createOrder(deliveryAddress: String): AppResult<Order> {
        if (deliveryAddress.isBlank()) {
            return AppResult.Failure(code = "VALIDATION_ERROR", message = "Delivery address must not be blank.")
        }

        val cart = cartDao.getCartOnce()
        if (cart.isEmpty()) {
            return AppResult.Failure(code = "VALIDATION_ERROR", message = "Cart must not be empty.")
        }

        return runCatching {
            val orderDto = foodApi.createOrder(
                CreateOrderRequestDto(
                    restaurantId = cart.first().restaurantId,
                    items = cart.map {
                        CreateOrderItemRequestDto(
                            menuItemId = it.menuItemId,
                            quantity = it.quantity,
                        )
                    },
                    deliveryAddress = deliveryAddress.trim(),
                ),
            )
            saveOrder(orderDto)
            cartDao.clearCart()
            requireNotNull(orderDto.toEntity()) { "Order response is missing required ids." }
                .toDomain(orderDto.items.mapNotNull { it.toEntity() })
        }.fold(
            onSuccess = { AppResult.Success(it) },
            onFailure = { it.toFailure("Could not create order.") },
        )
    }

    override fun observeOrders(): Flow<List<Order>> =
        orderDao.observeOrders().map { orders ->
            orders.map { order ->
                order.toDomain(orderDao.getOrderItems(order.id))
            }
        }

    override suspend fun refreshMyOrders(): AppResult<Unit> =
        runCatching {
            val orders = foodApi.getMyOrders()
            orderDao.clearOrders()
            orders.forEach { saveOrder(it) }
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { it.toFailure("Could not load orders.") },
        )

    override suspend fun getOrderById(orderId: String): AppResult<Order> {
        orderDao.getOrderById(orderId)?.let { order ->
            return AppResult.Success(order.toDomain(orderDao.getOrderItems(orderId)))
        }

        return runCatching {
            val orderDto = foodApi.getOrder(orderId)
            saveOrder(orderDto)
            requireNotNull(orderDto.toEntity()) { "Order response is missing required ids." }
                .toDomain(orderDto.items.mapNotNull { it.toEntity() })
        }.fold(
            onSuccess = { AppResult.Success(it) },
            onFailure = { it.toFailure("Could not load order.") },
        )
    }

    private suspend fun saveOrder(order: com.example.kursovayakotlin.data.remote.dto.OrderDto) {
        order.toEntity()?.let { orderDao.upsertOrders(listOf(it)) }
        orderDao.upsertOrderItems(order.items.mapNotNull { it.toEntity() })
    }

    private fun Throwable.toFailure(fallback: String): AppResult.Failure {
        if (this is HttpException) {
            val errorBody = response()?.errorBody()?.string().orEmpty()
            if (errorBody.contains("USER_NOT_SYNCED")) {
                return AppResult.Failure(
                    code = "USER_NOT_SYNCED",
                    message = "Profile is not synced yet. Please try again.",
                    cause = this,
                )
            }
            if (code() == 401) {
                return AppResult.Failure(
                    code = "UNAUTHORIZED",
                    message = "Authentication failed. Please sign in again.",
                    cause = this,
                )
            }
        }

        return AppResult.Failure(message = message ?: fallback, cause = this)
    }
}
