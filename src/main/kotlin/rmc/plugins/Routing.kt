package rmc.plugins

import rmc.route.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import rmc.route.user.validateTokenRoute

fun Application.configureRouting() {
    routing {
        userRoutes()
        validateTokenRoute()
    }
}

