package com.example.kursovayakotlin.data.remote.api

import com.example.kursovayakotlin.data.remote.dto.CreateOrderRequestDto
import com.example.kursovayakotlin.data.remote.dto.HealthDto
import com.example.kursovayakotlin.data.remote.dto.MenuItemDto
import com.example.kursovayakotlin.data.remote.dto.OrderDto
import com.example.kursovayakotlin.data.remote.dto.RestaurantDto
import com.example.kursovayakotlin.data.remote.dto.SyncUserRequestDto
import com.example.kursovayakotlin.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FoodApi {
    @GET("health")
    suspend fun getHealth(): HealthDto

    @GET("restaurants")
    suspend fun getRestaurants(): List<RestaurantDto>

    @GET("restaurants/{id}")
    suspend fun getRestaurant(
        @Path("id") id: String,
    ): RestaurantDto

    @GET("restaurants/{id}/menu")
    suspend fun getMenu(
        @Path("id") restaurantId: String,
    ): List<MenuItemDto>

    @GET("me")
    suspend fun getMe(): UserDto

    @POST("me/sync")
    suspend fun syncMe(
        @Body request: SyncUserRequestDto,
    ): UserDto

    @POST("orders")
    suspend fun createOrder(
        @Body request: CreateOrderRequestDto,
    ): OrderDto

    @GET("orders/my")
    suspend fun getMyOrders(): List<OrderDto>

    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") id: String,
    ): OrderDto
}
