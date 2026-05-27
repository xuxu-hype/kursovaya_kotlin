package com.example.kursovayakotlin.presentation.navigation

sealed class Screen(
    val route: String,
    val title: String,
) {
    data object Restaurants : Screen("restaurants", "Restaurants")
    data object Cart : Screen("cart", "Cart")
    data object Orders : Screen("orders", "Orders")
    data object Profile : Screen("profile", "Profile")

    data object Menu : Screen("menu/{restaurantId}", "Menu") {
        const val RESTAURANT_ID = "restaurantId"

        fun createRoute(restaurantId: String) = "menu/$restaurantId"
    }

    data object OrderDetails : Screen("orders/{orderId}", "Order Details") {
        const val ORDER_ID = "orderId"

        fun createRoute(orderId: String) = "orders/$orderId"
    }
}
