package com.example.kursovayakotlin.presentation.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun MainBottomBar(
    currentDestination: NavDestination?,
    onNavigate: (Screen) -> Unit,
) {
    val items = listOf(
        Screen.Restaurants,
        Screen.Cart,
        Screen.Orders,
        Screen.Profile,
    )

    NavigationBar {
        items.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(screen) },
                icon = { Text(screen.title.first().toString()) },
                label = { Text(screen.title) },
            )
        }
    }
}
