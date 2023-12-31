package rmc.route.rental

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
                    val id = call.parameters["id"]?.toInt() ?: throw throw WrongFormat("id")
                    val rental = rentalRepository.getRentalById(id)
                    if (userId != rental.userId) throw NotOwnerOfEntityWithId("rental", id)

                    val found = rentalRepository.getRentalById(id)
                    found.let { call.respond(it) }
                }

                get("/rentedVehicle/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val id = call.parameters["id"]?.toInt() ?: throw throw WrongFormat("id")

                    // Filter results to rentals matched to the userId of client
                    val found = rentalRepository.getRentalByVehicleId(id).filter { it.userId == userId }
                    found.let { call.respond(it) }
                }

                get("/ownedVehicle/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val id = call.parameters["id"]?.toInt() ?: throw throw WrongFormat("id")

                    // Filter results to rentals matched to the userId of client
                    val found = rentalRepository.getRentalByVehicleId(id)
                    val vehicle = vehicleRepository.getVehicleById(id)
                    if (userId != vehicle.userId) throw NotOwnerOfEntityWithId("vehicle", id)

                    found.let { call.respond(it) }
                }

                post("/createRental/{vehicleId}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val createRentalDTO = call.receive<CreateRentalDTO>()
                    val vehicleId = call.parameters["vehicleId"]?.toInt() ?: throw throw WrongFormat("id")

                    val vehicle = vehicleRepository.getVehicleById(vehicleId)

                    // Check if vehicle availability is true
                    if (!vehicle.availability) throw VehicleNotAvailable(vehicleId)

                    // Check if vehicle has rentals, if true
                    // Check if vehicle does not have an active rental for that date with status APPROVED
                    val rentals = rentalRepository.getRentalByVehicleId(vehicleId)
                    if (rentals.any { it.date == createRentalDTO.date && it.status == RentalStatus.APPROVED }) throw VehicleIsAlreadyRented(vehicleId, createRentalDTO.date)

                    val rental = rentalRepository.createRental(userId, vehicleId, createRentalDTO)
                    call.respondText(Json.encodeToString(rental), status = HttpStatusCode.Created)
                }

                get("/status/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val rentalId = call.parameters["id"]?.toInt() ?: throw WrongFormat("id")
                    val inputStatus = call.request.queryParameters["status"]?.uppercase() ?: throw StatusNotFound()
                    val enumStatus = RentalStatus.valueOf(inputStatus)

                    val rental = rentalRepository.getRentalById(rentalId)
                    val vehicle = vehicleRepository.getVehicleById(rental.vehicleId)

                    // Check if user should be able to change rental to given status
                    if (enumStatus == RentalStatus.APPROVED || enumStatus == RentalStatus.DENIED) {
                        if (userId != vehicle.userId) throw NotOwnerOfEntityWithId("vehicle", vehicle.id)
                    }
                    if (enumStatus == RentalStatus.CANCELLED) {
                        if (userId != rental.userId) throw NotOwnerOfEntityWithId("rental", rentalId)
                    }

                    rentalRepository.updateRentalStatus(rentalId, enumStatus)
                    rentalRepository.cascadeRentalStatus(rentalId, vehicle.id, rental.date)
                    println(enumStatus)
                    val updatedRental = rentalRepository.getRentalById(rentalId)
                    call.respond(updatedRental)
                }

                delete("/{id}") {
                    // Auth user
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    // Check if user is owner of rental
                    val id = call.parameters["id"]?.toInt() ?: throw WrongFormat("id")
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
                    val id = call.parameters["id"]?.toInt() ?: throw throw WrongFormat("id")

                    rentalRepository.updateRental(id, updateRentalDTO)
                    val updatedRental = rentalRepository.getRentalById(id)
                    call.respond(updatedRental)
                }

                delete("/staff/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.STAFF, user)

                    val id = call.parameters["id"]?.toInt() ?: throw throw WrongFormat("id")
                    call.respond(rentalRepository.deleteRental(id))
                }
            }
        }
    }
}
