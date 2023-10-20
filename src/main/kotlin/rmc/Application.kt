package rmc

import io.ktor.server.application.*
import io.ktor.server.netty.*
import rmc.db.DatabaseFactory
import rmc.error.configureStatusPages
import rmc.plugins.configureRouting
import rmc.plugins.configureSecurity
import rmc.plugins.configureSerialization
import rmc.plugins.configureValidation

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureStatusPages()
    configureValidation()
    DatabaseFactory.init(environment.config)
}

//    TODO("TEST HTTP ENDPONTS")
//    TODO("VALIDATE USER INPUT DTOS?")
//    TODO("SEPARATE USER AND USERPROFILE ROUTES")
//    TODO("CHECK EACH FILE FOR UNUSED IMPORTS AND ERRORS")
