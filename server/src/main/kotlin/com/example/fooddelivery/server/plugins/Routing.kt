package com.example.fooddelivery.server.plugins

import com.example.fooddelivery.server.data.repository.ExposedOrderRepository
import com.example.fooddelivery.server.data.repository.ExposedRestaurantRepository
import com.example.fooddelivery.server.data.repository.ExposedUserRepository
import com.example.fooddelivery.server.domain.repository.OrderRepository
import com.example.fooddelivery.server.domain.repository.RestaurantRepository
import com.example.fooddelivery.server.domain.repository.UserRepository
import com.example.fooddelivery.server.domain.usecase.CreateOrderUseCase
import com.example.fooddelivery.server.domain.usecase.GetMenuUseCase
import com.example.fooddelivery.server.domain.usecase.GetMyOrdersUseCase
import com.example.fooddelivery.server.domain.usecase.GetOrderByIdUseCase
import com.example.fooddelivery.server.domain.usecase.GetRestaurantByIdUseCase
import com.example.fooddelivery.server.domain.usecase.GetRestaurantsUseCase
import com.example.fooddelivery.server.domain.usecase.SyncUserUseCase
import com.example.fooddelivery.server.routes.registerOrderRoutes
import com.example.fooddelivery.server.routes.registerHealthRoutes
import com.example.fooddelivery.server.routes.registerRestaurantRoutes
import com.example.fooddelivery.server.routes.registerUserRoutes
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing

data class ApplicationDependencies(
    val userRepository: UserRepository,
    val restaurantRepository: RestaurantRepository,
    val orderRepository: OrderRepository,
) {
    val syncUser = SyncUserUseCase(userRepository)
    val getRestaurants = GetRestaurantsUseCase(restaurantRepository)
    val getRestaurantById = GetRestaurantByIdUseCase(restaurantRepository)
    val getMenu = GetMenuUseCase(restaurantRepository)
    val createOrder = CreateOrderUseCase(restaurantRepository, orderRepository)
    val getMyOrders = GetMyOrdersUseCase(orderRepository)
    val getOrderById = GetOrderByIdUseCase(orderRepository)
}

fun defaultApplicationDependencies(): ApplicationDependencies =
    ApplicationDependencies(
        userRepository = ExposedUserRepository(),
        restaurantRepository = ExposedRestaurantRepository(),
        orderRepository = ExposedOrderRepository(),
    )

fun Application.configureRouting(
    dependencies: ApplicationDependencies = defaultApplicationDependencies(),
    installProtectedRoutes: Boolean = true,
) {
    routing {
        registerHealthRoutes()
        registerRestaurantRoutes(
            getRestaurants = dependencies.getRestaurants,
            getRestaurantById = dependencies.getRestaurantById,
            getMenu = dependencies.getMenu,
        )

        if (installProtectedRoutes) {
            authenticate("firebase") {
                registerUserRoutes(
                    syncUser = dependencies.syncUser,
                    userRepository = dependencies.userRepository,
                )
                registerOrderRoutes(
                    createOrder = dependencies.createOrder,
                    getMyOrders = dependencies.getMyOrders,
                    getOrderById = dependencies.getOrderById,
                    userRepository = dependencies.userRepository,
                )
            }
        }
    }
}
