package rmc

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import rmc.db.DatabaseFactory
import rmc.error.configureStatusPages
import rmc.plugins.*

fun main() {
    embeddedServer(Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureStatusPages()
    configureValidation()
    DatabaseFactory.init()
}

//    TODO("SLIDES LES 7, JSON ATTRIBUTE FOR LOCATION IN RENTAL AND VEHICLES TABLE (LAT/LONG) ")
//    TODO("SEPARATE USER AND USERPROFILE ROUTES")
