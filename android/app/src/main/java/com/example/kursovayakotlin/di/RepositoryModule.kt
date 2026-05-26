package com.example.kursovayakotlin.di

import com.example.kursovayakotlin.data.repository.AuthRepositoryImpl
import com.example.kursovayakotlin.data.repository.CartRepositoryImpl
import com.example.kursovayakotlin.data.repository.OrderRepositoryImpl
import com.example.kursovayakotlin.data.repository.RestaurantRepositoryImpl
import com.example.kursovayakotlin.domain.repository.AuthRepository
import com.example.kursovayakotlin.domain.repository.CartRepository
import com.example.kursovayakotlin.domain.repository.OrderRepository
import com.example.kursovayakotlin.domain.repository.RestaurantRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRestaurantRepository(impl: RestaurantRepositoryImpl): RestaurantRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth =
            FirebaseAuth.getInstance()
    }
}
