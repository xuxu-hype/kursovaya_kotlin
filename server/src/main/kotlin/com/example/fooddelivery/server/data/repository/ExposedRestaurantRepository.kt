package com.example.fooddelivery.server.data.repository

import com.example.fooddelivery.server.data.db.tables.MenuItemsTable
import com.example.fooddelivery.server.data.db.tables.RestaurantsTable
import com.example.fooddelivery.server.data.mapper.toMenuItem
import com.example.fooddelivery.server.data.mapper.toRestaurant
import com.example.fooddelivery.server.domain.model.MenuItem
import com.example.fooddelivery.server.domain.model.Restaurant
import com.example.fooddelivery.server.domain.repository.RestaurantRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedRestaurantRepository : RestaurantRepository {
    override fun findAll(): List<Restaurant> =
        transaction {
            RestaurantsTable
                .selectAll()
                .orderBy(RestaurantsTable.name to SortOrder.ASC)
                .map { it.toRestaurant() }
        }

    override fun findById(id: UUID): Restaurant? =
        transaction {
            RestaurantsTable
                .selectAll()
                .where { RestaurantsTable.id eq id }
                .singleOrNull()
                ?.toRestaurant()
        }

    override fun findMenuByRestaurantId(restaurantId: UUID): List<MenuItem> =
        transaction {
            MenuItemsTable
                .selectAll()
                .where { MenuItemsTable.restaurantId eq restaurantId }
                .orderBy(MenuItemsTable.name to SortOrder.ASC)
                .map { it.toMenuItem() }
        }

    override fun findMenuItemById(id: UUID): MenuItem? =
        transaction {
            MenuItemsTable
                .selectAll()
                .where { MenuItemsTable.id eq id }
                .singleOrNull()
                ?.toMenuItem()
        }
}
