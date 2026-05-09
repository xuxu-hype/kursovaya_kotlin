package com.example.fooddelivery.server.db

import com.example.fooddelivery.server.config.AppConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory
import javax.sql.DataSource

object DatabaseFactory {
    private val logger = LoggerFactory.getLogger(DatabaseFactory::class.java)
    private var dataSource: HikariDataSource? = null

    fun init(config: AppConfig) {
        if (dataSource != null) return

        val ds = HikariDataSource(buildHikariConfig(config))
        Flyway.configure()
            .dataSource(ds)
            .locations("classpath:db/migration")
            .load()
            .migrate()
        Database.connect(ds)
        dataSource = ds
        logger.info("Database pool started and Flyway migrations applied.")
    }

    fun shutdown() {
        dataSource?.close()
        dataSource = null
    }

    /** Visible for integration tests that need a [DataSource]. */
    internal fun dataSourceOrNull(): DataSource? = dataSource

    private fun buildHikariConfig(config: AppConfig): HikariConfig =
        HikariConfig().apply {
            jdbcUrl = config.databaseUrl
            username = config.databaseUser
            password = config.databasePassword
            maximumPoolSize = 10
            poolName = "fooddelivery-pool"
        }
}
