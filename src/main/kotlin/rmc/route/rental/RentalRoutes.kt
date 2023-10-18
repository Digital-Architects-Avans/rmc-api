package rmc.route.rental

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rmc.db.dao.UserType
import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.UpdateRentalDTO
import rmc.error.NotOwnerOfEntityWithId
import rmc.error.StatusNotFound
import rmc.error.WrongIdFormatException
import rmc.plugins.authorize
import rmc.plugins.currentUserId
import rmc.repository.rental.RentalRepositoryImpl
import rmc.repository.user.userRepository

fun Route.rentalRoutes() {
    val rentalRepository = RentalRepositoryImpl()

    route("/rental") {
        rateLimit {
            authenticate {
                get("/rentals") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val rentals = rentalRepository.getRentalByUserId(userId)
                    call.respond(rentals)
                }

                get("/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    // Check if user is owner of rental
                    val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                    val rental = rentalRepository.getRentalById(id)
                    if (userId != rental.userId) throw NotOwnerOfEntityWithId("rental", id)

                    val found = rentalRepository.getRentalById(id)
                    found.let { call.respond(it) }
                }

                get("/vehicle/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()

                    // Filter results to rentals matched to the userId of client
                    val found = rentalRepository.getRentalByVehicleId(id).filter { it.userId == userId }
                    found.let { call.respond(it) }
                }

                post("/createRental/{vehicleId}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val createRentalDTO = call.receive<CreateRentalDTO>()
                    val vehicleId = call.parameters["vehicleId"]?.toInt() ?: throw WrongIdFormatException()

                    TODO("Check if vehicle availability == true")
//                    val vehicle = vehicleRepository.getVehicleById(vehicleId)
//
//                    if (vehicle.availability != true) throw vehicleNotAvailable()
//                    val vehicle = rentalRepository.createRental(userId, vehicleId, createRentalDTO)
//                    call.respond(vehicle)
                }

                put("/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val updateRentalDTO = call.receive<UpdateRentalDTO>()

                    // Check if user is owner of rental
                    val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                    val rental = rentalRepository.getRentalById(id)
                    if (userId != rental.userId) throw NotOwnerOfEntityWithId("rental", id)

                    rentalRepository.updateRental(id, updateRentalDTO)
                    val updatedRental = rentalRepository.getRentalById(id)
                    call.respond(updatedRental)
                }

                put("/{id}/{status}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                    val status = call.request.queryParameters["status"]?.lowercase() ?: throw StatusNotFound()

                    TODO("Check if user is owner of rental (cancel) || owner of the vehicle in the rental (accept/deny)")
//                    val rental = rentalRepository.getRentalById(id)
//                    val vehicle = vehicleRepository.getVehicleById(rental.vehicleId)
//                    if (userId != rental.userId || vehicle.userId) throw NotOwnerOfEntityWithId("rental", id)
//
//                    rentalRepository.updateRentalStatus(id, RentalStatus.valueOf(status))
//                    val updatedRental = rentalRepository.getRentalById(id)
//                    call.respond(updatedRental)
                }

                delete("/{id}") {
                    // Auth user
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    // Check if user is owner of rental
                    val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                    val rental = rentalRepository.getRentalById(id)
                    if (userId != rental.userId) throw NotOwnerOfEntityWithId("rental", id)

                    call.respond(rentalRepository.deleteRental(id))
                }

                get("/allRentals") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.STAFF, user)

                    val rentals = rentalRepository.allRentals()
                    call.respond(rentals)
                }

                put("/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.STAFF, user)

                    val updateRentalDTO = call.receive<UpdateRentalDTO>()
                    val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()

                    rentalRepository.updateRental(id, updateRentalDTO)
                    val updatedRental = rentalRepository.getRentalById(id)
                    call.respond(updatedRental)
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
}