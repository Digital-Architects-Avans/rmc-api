package rmc.route.vehicle

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rmc.db.dao.UserType
import rmc.dto.vehicle.CreateVehicleDTO
import rmc.dto.vehicle.UpdateVehicleDTO
import rmc.error.NotOwnerOfEntityWithId
import rmc.error.WrongFormat
import rmc.plugins.authorize
import rmc.plugins.currentUserId
import rmc.repository.user.userRepository
import rmc.repository.vehicle.VehicleRepositoryImpl

fun Route.vehicleRoutes() {
    val vehicleRepository = VehicleRepositoryImpl()

    authenticate {
        route("/vehicle") {
            post("/createVehicle") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.CLIENT, user)

                val createVehicleDTO = call.receive<CreateVehicleDTO>()

                val vehicle = vehicleRepository.createVehicle(userId, createVehicleDTO)
                call.respond(vehicle)
            }

            get("/{id}") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.CLIENT, user)

                val id = call.parameters["id"]?.toInt() ?: throw throw WrongFormat("id")
                val found = vehicleRepository.getVehicleById(id)
                found.let { call.respond(it) }
            }

            get("/all") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.CLIENT, user)

                val vehicles = vehicleRepository.getAllVehicles()
                call.respond(vehicles)
            }

            get("/allAvailable") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.CLIENT, user)

                val vehicles = vehicleRepository.getAllAvailableVehicles()
                call.respond(vehicles)
            }

            get("/user") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.CLIENT, user)

                val found = vehicleRepository.getVehiclesByUserId(userId)
                found.let { call.respond(it) }
            }

            put("/{id}") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.CLIENT, user)

                val vehicleId = call.parameters["id"]?.toInt() ?: throw throw WrongFormat("id")
                val updateVehicleDTO = call.receive<UpdateVehicleDTO>()

                // Check if user is owner of vehicle
                val vehicle = vehicleRepository.getVehicleById(vehicleId)
                if (userId != vehicle.userId) throw NotOwnerOfEntityWithId("vehicle", userId)

                vehicleRepository.updateVehicle(vehicleId, updateVehicleDTO)
                val updatedVehicle = vehicleRepository.getVehicleById(vehicleId)
                call.respond(updatedVehicle)
            }

            put("/setAvailability/{id}/{availability}") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.CLIENT, user)

                val vehicleId = call.parameters["id"]?.toInt() ?: throw WrongFormat("id")
                val availability = call.parameters["availability"]?.toBoolean() ?: throw WrongFormat("Boolean")

                // Check if user is owner of vehicle
                val vehicle = vehicleRepository.getVehicleById(vehicleId)
                if (userId != vehicle.userId) throw NotOwnerOfEntityWithId("vehicle", userId)

                vehicleRepository.setVehicleAvailability(vehicleId, availability)
                val updatedVehicle = vehicleRepository.getVehicleById(vehicleId)
                call.respond(updatedVehicle)
            }

            delete("/{id}") {
                val userId = currentUserId()
                val user = userRepository.getUserById(userId)
                authorize(UserType.CLIENT, user)

                // Check if user is owner of vehicle
                val vehicleId = call.parameters["id"]?.toInt() ?: throw WrongFormat("id")
                val vehicle = vehicleRepository.getVehicleById(vehicleId)
                if (userId != vehicle.userId) throw NotOwnerOfEntityWithId("vehicle", userId)

                // Cancel all rentals with this vehicleId

                call.respond(vehicleRepository.deleteVehicle(vehicleId))
            }
        }
    }
}