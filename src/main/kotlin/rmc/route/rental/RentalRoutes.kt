package rmc.route.rental

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rmc.db.dao.RentalStatus
import rmc.db.dao.UserType
import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.UpdateRentalDTO
import rmc.error.*
import rmc.plugins.authorize
import rmc.plugins.currentUserId
import rmc.repository.rental.RentalRepositoryImpl
import rmc.repository.user.userRepository
import rmc.repository.vehicle.vehicleRepository

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

                    val vehicle = vehicleRepository.getVehicleById(vehicleId)

                    // Check if vehicle availability is true
                    if (!vehicle.availability) throw VehicleNotAvailable(vehicleId)

                    // Check if vehicle has rentals, if true
                    // Check if vehicle does not have an active rental for that date
                    val rentals = rentalRepository.getRentalByVehicleId(vehicleId)
                    if (rentals.any { it.date == createRentalDTO.date }) throw VehicleIsAlreadyRented(vehicleId, createRentalDTO.date)

                    call.respond(rentalRepository.createRental(userId, vehicleId, createRentalDTO))

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

                    val rentalId = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                    val inputStatus = call.request.queryParameters["status"]?.lowercase() ?: throw StatusNotFound()
                    val enumStatus = RentalStatus.valueOf(inputStatus)

                    val rental = rentalRepository.getRentalById(rentalId)
                    val vehicle = vehicleRepository.getVehicleById(rental.vehicleId)

                    // Check if user should be able to change rental to given status
                    if (enumStatus == RentalStatus.APPROVED || enumStatus == RentalStatus.DENIED) {
                        if (userId != vehicle.userId) throw NotOwnerOfEntityWithId("vehicle", vehicle.id)
                    }
                    if (enumStatus == RentalStatus.DENIED) {
                        if (userId != rental.userId) throw NotOwnerOfEntityWithId("rental", rentalId)
                    }

                    rentalRepository.updateRentalStatus(rentalId, enumStatus)
                    val updatedRental = rentalRepository.getRentalById(rentalId)
                    call.respond(updatedRental)
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
