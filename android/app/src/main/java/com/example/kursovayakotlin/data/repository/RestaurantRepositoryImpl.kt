package com.example.kursovayakotlin.data.repository

import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.data.local.dao.MenuItemDao
import com.example.kursovayakotlin.data.local.dao.RestaurantDao
import com.example.kursovayakotlin.data.mapper.toDomain
import com.example.kursovayakotlin.data.mapper.toEntity
import com.example.kursovayakotlin.data.remote.api.FoodApi
import com.example.kursovayakotlin.domain.model.MenuItem
import com.example.kursovayakotlin.domain.model.Restaurant
import com.example.kursovayakotlin.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val foodApi: FoodApi,
    private val restaurantDao: RestaurantDao,
    private val menuItemDao: MenuItemDao,
) : RestaurantRepository {
    override fun observeRestaurants(): Flow<List<Restaurant>> =
        restaurantDao.observeRestaurants().map { restaurants ->
            restaurants.map { it.toDomain() }
        }

    override suspend fun refreshRestaurants(): AppResult<Unit> =
        runCatching {
            restaurantDao.upsertRestaurants(foodApi.getRestaurants().map { it.toEntity() })
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )

    override suspend fun getRestaurantById(id: String): AppResult<Restaurant> {
        restaurantDao.getRestaurantById(id)?.let {
            return AppResult.Success(it.toDomain())
        }

        return runCatching {
            val restaurant = foodApi.getRestaurant(id).toEntity()
            restaurantDao.upsertRestaurants(listOf(restaurant))
            restaurant.toDomain()
        }.fold(
            onSuccess = { AppResult.Success(it) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )
    }

    override fun observeMenu(restaurantId: String): Flow<List<MenuItem>> =
        menuItemDao.observeMenuByRestaurant(restaurantId).map { menu ->
            menu.map { it.toDomain() }
        }

    override suspend fun refreshMenu(restaurantId: String): AppResult<Unit> =
        runCatching {
            val menu = foodApi.getMenu(restaurantId).mapNotNull { it.toEntity() }
            menuItemDao.clearMenuForRestaurant(restaurantId)
            menuItemDao.upsertMenuItems(menu)
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )
}
