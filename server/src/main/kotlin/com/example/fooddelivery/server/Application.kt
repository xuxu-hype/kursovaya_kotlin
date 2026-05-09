package com.example.fooddelivery.server

import com.example.fooddelivery.server.auth.FirebaseAuthService
import com.example.fooddelivery.server.auth.installFirebaseAuth
import com.example.fooddelivery.server.config.AppConfig
import com.example.fooddelivery.server.db.DatabaseFactory
import com.example.fooddelivery.server.plugins.configureHTTP
import com.example.fooddelivery.server.plugins.configureRouting
import com.example.fooddelivery.server.plugins.configureSerialization
import com.example.fooddelivery.server.plugins.configureStatusPages
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val appConfig = AppConfig.fromEnvironment()

    configureHTTP()
    configureSerialization()
    configureStatusPages()

    monitor.subscribe(ApplicationStarted) {
        DatabaseFactory.init(appConfig)
    }
    monitor.subscribe(ApplicationStopped) {
        DatabaseFactory.shutdown()
    }

    val firebaseAuth = FirebaseAuthService.create(appConfig)
    installFirebaseAuth(firebaseAuth)

    configureRouting()
}
