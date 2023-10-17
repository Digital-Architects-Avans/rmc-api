package rmc.route.rental

import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rmc.db.dao.UserType
import rmc.dto.rental.CreateRentalDTO
import rmc.error.WrongIdFormatException
import rmc.plugins.authorize
import rmc.plugins.currentUserId
import rmc.repository.rental.RentalRepositoryImpl
import rmc.repository.user.userRepository


fun Route.rentalRoutes() {
    val rentalRepository = RentalRepositoryImpl()

    route("/rental") {
        rateLimit {
            get("/rentals") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.STAFF, user)

                val rentals = rentalRepository.allRentals()
                call.respond(rentals)
            }

            get("/{id}") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.STAFF, user)

                val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                val found = rentalRepository.getRentalById(id)
                found.let { call.respond(it) }
            }

            post("/createRental/{vehicleId}") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.STAFF, user)

                val createRentalDTO = call.receive<CreateRentalDTO>()
                val vehicleId = call.parameters["vehicleId"]?.toInt() ?: throw WrongIdFormatException()

                val vehicle = rentalRepository.createRental(userId, vehicleId, createRentalDTO)
                call.respond(vehicle)
            }

            delete("/{id}") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.STAFF, user)

                val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                call.respond(rentalRepository.deleteRental(id))
            }
        }
    }
}