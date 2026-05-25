package com.example.kursovayakotlin.di

import android.content.Context
import androidx.room.Room
import com.example.kursovayakotlin.data.local.dao.CartDao
import com.example.kursovayakotlin.data.local.dao.MenuItemDao
import com.example.kursovayakotlin.data.local.dao.OrderDao
import com.example.kursovayakotlin.data.local.dao.RestaurantDao
import com.example.kursovayakotlin.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "food_delivery.db",
        ).build()

    @Provides
    fun provideRestaurantDao(database: AppDatabase): RestaurantDao =
        database.restaurantDao()

    @Provides
    fun provideMenuItemDao(database: AppDatabase): MenuItemDao =
        database.menuItemDao()

    @Provides
    fun provideCartDao(database: AppDatabase): CartDao =
        database.cartDao()

    @Provides
    fun provideOrderDao(database: AppDatabase): OrderDao =
        database.orderDao()
}
