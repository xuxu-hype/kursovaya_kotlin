package com.example.kursovayakotlin.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.data.local.db.AppDatabase
import com.example.kursovayakotlin.domain.model.MenuItem
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CartRepositoryImplTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: CartRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = CartRepositoryImpl(database.cartDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `adding item from another restaurant clears previous cart`() = runTest {
        assertTrue(repository.addToCart(menuItem(id = "menu-1", restaurantId = "restaurant-1")) is AppResult.Success)

        val result = repository.addToCart(menuItem(id = "menu-2", restaurantId = "restaurant-2"))

        assertTrue(result is AppResult.Success)
        val cart = (repository.getCartOnce() as AppResult.Success).data
        assertEquals(1, cart.size)
        assertEquals("menu-2", cart.single().menuItemId)
        assertEquals("restaurant-2", cart.single().restaurantId)
    }

    private fun menuItem(
        id: String,
        restaurantId: String,
    ): MenuItem =
        MenuItem(
            id = id,
            restaurantId = restaurantId,
            name = id,
            description = null,
            priceCents = 1000,
            imageUrl = null,
            isAvailable = true,
        )
}
