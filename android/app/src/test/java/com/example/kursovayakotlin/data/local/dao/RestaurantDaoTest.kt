package com.example.kursovayakotlin.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.kursovayakotlin.data.local.db.AppDatabase
import com.example.kursovayakotlin.data.local.entity.RestaurantEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RestaurantDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: RestaurantDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.restaurantDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insert restaurants`() = runTest {
        dao.upsertRestaurants(restaurants())

        assertEquals(restaurants(), dao.getRestaurantsOnce())
    }

    @Test
    fun `observe and get restaurants`() = runTest {
        dao.upsertRestaurants(restaurants())

        assertEquals(restaurants(), dao.observeRestaurants().first())
        assertEquals(restaurants(), dao.getRestaurantsOnce())
    }

    @Test
    fun `get restaurant by id`() = runTest {
        dao.upsertRestaurants(restaurants())

        assertEquals(restaurants().first(), dao.getRestaurantById("restaurant-1"))
    }

    private fun restaurants(): List<RestaurantEntity> =
        listOf(
            RestaurantEntity(
                id = "restaurant-1",
                name = "Tokyo Bento",
                description = "Japanese comfort food.",
                imageUrl = null,
                address = "12 Sakura Street",
                rating = 4.7,
                isOpen = true,
            ),
            RestaurantEntity(
                id = "restaurant-2",
                name = "Pasta House",
                description = null,
                imageUrl = "https://example.com/pasta.jpg",
                address = null,
                rating = 4.5,
                isOpen = false,
            ),
        )
}
