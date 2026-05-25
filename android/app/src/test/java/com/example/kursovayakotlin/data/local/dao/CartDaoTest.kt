package com.example.kursovayakotlin.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.kursovayakotlin.data.local.db.AppDatabase
import com.example.kursovayakotlin.data.local.entity.CartItemEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CartDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: CartDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.cartDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insert cart item`() = runTest {
        dao.upsertCartItem(cartItem())

        assertEquals(cartItem(), dao.getCartItem("menu-1"))
        assertEquals(listOf(cartItem()), dao.getCartOnce())
    }

    @Test
    fun `update quantity`() = runTest {
        dao.upsertCartItem(cartItem())
        dao.updateQuantity(menuItemId = "menu-1", quantity = 3)

        assertEquals(3, dao.getCartItem("menu-1")?.quantity)
    }

    @Test
    fun `clear cart`() = runTest {
        dao.upsertCartItem(cartItem())
        dao.clearCart()

        assertNull(dao.getCartItem("menu-1"))
        assertEquals(emptyList<CartItemEntity>(), dao.getCartOnce())
    }

    private fun cartItem(): CartItemEntity =
        CartItemEntity(
            menuItemId = "menu-1",
            restaurantId = "restaurant-1",
            name = "Salmon Nigiri Set",
            priceCents = 1290,
            imageUrl = null,
            quantity = 1,
        )
}
