package rmc.plugins

import rmc.route.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import rmc.route.rental.rentalRoutes
import rmc.route.vehicle.vehicleRoutes

fun Application.configureRouting() {
    routing {
        userRoutes()
        vehicleRoutes()
        rentalRoutes()
    }
}

