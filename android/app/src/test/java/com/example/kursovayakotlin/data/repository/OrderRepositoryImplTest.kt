package com.example.kursovayakotlin.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.data.local.db.AppDatabase
import com.example.kursovayakotlin.domain.usecase.orders.CreateOrderUseCase
import com.example.kursovayakotlin.testutil.FakeFoodApi
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
class OrderRepositoryImplTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: OrderRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = OrderRepositoryImpl(
            foodApi = FakeFoodApi(),
            cartDao = database.cartDao(),
            orderDao = database.orderDao(),
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `create order use case returns error for blank delivery address`() = runTest {
        val result = CreateOrderUseCase(repository).invoke("   ")

        assertTrue(result is AppResult.Failure)
        assertEquals("VALIDATION_ERROR", (result as AppResult.Failure).code)
    }
}
