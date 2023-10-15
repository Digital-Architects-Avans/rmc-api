package rmc.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import java.io.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*
import rmc.db.tables.RentalsTable
import rmc.db.tables.UsersTable
import rmc.db.tables.VehiclesTable

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("storage.driverClassName").getString()
        val jdbcURL = config.property("storage.jdbcURL").getString() +
                (config.propertyOrNull("storage.dbFilePath")?.getString()?.let {
                    File(it).canonicalFile.absolutePath
                } ?: "")
        val database = Database.connect(createHikariDataSource(url = jdbcURL, driver = driverClassName))

        transaction(database) {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(
                UsersTable,
                VehiclesTable,
                RentalsTable
            )
        }
    }

    private fun createHikariDataSource(
        url: String,
        driver: String
    ) = HikariDataSource(HikariConfig().apply {
        driverClassName = driver
        jdbcUrl = url
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    // Utility function which we use for all requests to the DB
    // Starts each query in its own coroutine
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

