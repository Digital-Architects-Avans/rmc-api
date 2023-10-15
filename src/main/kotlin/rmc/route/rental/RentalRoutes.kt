package rmc.route.rental

import rmc.error.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rmc.dao.rental.RentalRepositoryImpl
import rmc.dto.rental.CreateRentalDTO

fun Route.rentalRoutes() {
    val rentalRepository = RentalRepositoryImpl()

    authenticate {
        post("createRental/{vehicleId}") {
            val createRentalDTO = call.receive<CreateRentalDTO>()
            val vehicleId = call.parameters["vehicleId"]?.toInt() ?: throw WrongIdFormatException()

            val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
            val userId = principal.payload.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()

            val vehicle = rentalRepository.createRental(userId, vehicleId, createRentalDTO)
            call.respond(vehicle)
        }
    }
}