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
//    TODO("VALIDATE ALL DTO'S WITH VALIDATE PLUGIN")
//    TODO("REPLACE REPEATING CODE FOR USERTYPE VALIDATION WITH AUTORIZE FUNC IN ROUTING (REMUS HOTKITCHEN)")
//    TODO("SEPARATE USER AND USERPROFILE ROUTES")
