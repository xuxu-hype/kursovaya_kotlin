package com.example.kursovayakotlin.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kursovayakotlin.presentation.auth.AuthScreen
import com.example.kursovayakotlin.presentation.auth.AuthViewModel
import com.example.kursovayakotlin.presentation.cart.CartScreen
import com.example.kursovayakotlin.presentation.menu.MenuScreen
import com.example.kursovayakotlin.presentation.orders.OrderDetailsScreen
import com.example.kursovayakotlin.presentation.orders.OrdersScreen
import com.example.kursovayakotlin.presentation.profile.ProfileScreen
import com.example.kursovayakotlin.presentation.restaurants.RestaurantsScreen

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val authState = authViewModel.uiState.collectAsStateWithLifecycle().value

    if (!authState.isAuthenticated) {
        AuthScreen(viewModel = authViewModel)
        return
    }

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value

    Scaffold(
        bottomBar = {
            MainBottomBar(
                currentDestination = navBackStackEntry?.destination,
                onNavigate = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Restaurants.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(Screen.Restaurants.route) {
                RestaurantsScreen(
                    onRestaurantClick = { restaurantId ->
                        navController.navigate(Screen.Menu.createRoute(restaurantId))
                    },
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    onOrderCreated = { orderId ->
                        navController.navigate(Screen.OrderDetails.createRoute(orderId)) {
                            popUpTo(Screen.Cart.route)
                        }
                    },
                )
            }
            composable(Screen.Orders.route) {
                OrdersScreen(
                    onOrderClick = { orderId ->
                        navController.navigate(Screen.OrderDetails.createRoute(orderId))
                    },
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(
                route = Screen.Menu.route,
                arguments = listOf(navArgument(Screen.Menu.RESTAURANT_ID) { type = NavType.StringType }),
            ) {
                MenuScreen(onBackClick = { navController.popBackStack() })
            }
            composable(
                route = Screen.OrderDetails.route,
                arguments = listOf(navArgument(Screen.OrderDetails.ORDER_ID) { type = NavType.StringType }),
            ) {
                OrderDetailsScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}
