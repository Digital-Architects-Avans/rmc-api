package rmc.route.rental

import rmc.error.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rmc.dao.rental.RentalRepositoryImpl
import rmc.dto.rental.CreateRentalDTO

fun Route.rentalRoutes() {
    val rentalRepository = RentalRepositoryImpl()

    post("createRental/{userId}/{vehicleId}") {
        val createRentalDTO = call.receive<CreateRentalDTO>()
        val userId = call.parameters["userId"]?.toInt() ?: throw WrongIdFormatException()
        val vehicleId = call.parameters["vehicleId"]?.toInt() ?: throw WrongIdFormatException()


        val vehicle = rentalRepository.createRental(userId, vehicleId, createRentalDTO)
        call.respond(vehicle)

    }
}