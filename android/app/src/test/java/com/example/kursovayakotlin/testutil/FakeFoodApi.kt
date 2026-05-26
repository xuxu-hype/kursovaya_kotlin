package com.example.kursovayakotlin.testutil

import com.example.kursovayakotlin.data.remote.api.FoodApi
import com.example.kursovayakotlin.data.remote.dto.CreateOrderRequestDto
import com.example.kursovayakotlin.data.remote.dto.HealthDto
import com.example.kursovayakotlin.data.remote.dto.MenuItemDto
import com.example.kursovayakotlin.data.remote.dto.OrderDto
import com.example.kursovayakotlin.data.remote.dto.RestaurantDto
import com.example.kursovayakotlin.data.remote.dto.SyncUserRequestDto
import com.example.kursovayakotlin.data.remote.dto.UserDto

class FakeFoodApi : FoodApi {
    var createdOrderRequest: CreateOrderRequestDto? = null
    var orderToReturn: OrderDto? = null

    override suspend fun getHealth(): HealthDto =
        HealthDto(status = "OK")

    override suspend fun getRestaurants(): List<RestaurantDto> =
        emptyList()

    override suspend fun getRestaurant(id: String): RestaurantDto =
        error("Not used in this test.")

    override suspend fun getMenu(restaurantId: String): List<MenuItemDto> =
        emptyList()

    override suspend fun getMe(): UserDto =
        error("Not used in this test.")

    override suspend fun syncMe(request: SyncUserRequestDto): UserDto =
        error("Not used in this test.")

    override suspend fun createOrder(request: CreateOrderRequestDto): OrderDto {
        createdOrderRequest = request
        return orderToReturn ?: error("No fake order configured.")
    }

    override suspend fun getMyOrders(): List<OrderDto> =
        emptyList()

    override suspend fun getOrder(id: String): OrderDto =
        orderToReturn ?: error("No fake order configured.")
}
