package rmc

import io.ktor.server.application.*
import io.ktor.server.netty.*
import rmc.db.DatabaseFactory
import rmc.error.configureStatusPages
import rmc.plugins.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureStatusPages()
    configureValidation()
    DatabaseFactory.init(environment.config)
}

//    TODO("SLIDES LES 7, JSON ATTRIBUTE FOR LOCATION IN RENTAL AND VEHICLES TABLE (LAT/LONG) ")
//    TODO("SEPARATE USER AND USERPROFILE ROUTES")
