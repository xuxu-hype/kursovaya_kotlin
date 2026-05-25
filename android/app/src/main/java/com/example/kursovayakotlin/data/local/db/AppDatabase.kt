package com.example.kursovayakotlin.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kursovayakotlin.data.local.dao.CartDao
import com.example.kursovayakotlin.data.local.dao.MenuItemDao
import com.example.kursovayakotlin.data.local.dao.OrderDao
import com.example.kursovayakotlin.data.local.dao.RestaurantDao
import com.example.kursovayakotlin.data.local.entity.CartItemEntity
import com.example.kursovayakotlin.data.local.entity.MenuItemEntity
import com.example.kursovayakotlin.data.local.entity.OrderEntity
import com.example.kursovayakotlin.data.local.entity.OrderItemEntity
import com.example.kursovayakotlin.data.local.entity.RestaurantEntity

@Database(
    entities = [
        RestaurantEntity::class,
        MenuItemEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    abstract fun menuItemDao(): MenuItemDao

    abstract fun cartDao(): CartDao

    abstract fun orderDao(): OrderDao
}
