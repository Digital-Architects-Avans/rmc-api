package rmc.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import rmc.db.dao.UserType
import rmc.dto.user.UserDTO
import rmc.error.MissingPermissionError
import rmc.route.rental.rentalRoutes
import rmc.route.user.userRoutes
import rmc.route.vehicle.vehicleRoutes

fun Application.configureRouting() {
    routing {
        userRoutes()
        vehicleRoutes()
        rentalRoutes()
    }
}

fun PipelineContext<Unit, ApplicationCall>.currentUserId(): Int =
    call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asInt()

fun authorize(requiredUserType: UserType, user: UserDTO) =
    if (user.userType != requiredUserType) throw MissingPermissionError(requiredUserType) else null

