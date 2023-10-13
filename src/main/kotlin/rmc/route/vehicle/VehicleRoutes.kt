package rmc.route.vehicle

import rmc.error.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rmc.dao.vehicle.VehicleRepositoryImpl
import rmc.dto.vehicle.CreateVehicleDTO

fun Route.vehicleRoutes() {
    val vehicleRepository = VehicleRepositoryImpl()

    post("createVehicle/{userId}") {
        val createVehicleDTO = call.receive<CreateVehicleDTO>()
        val userId = call.parameters["userId"]?.toInt() ?: throw WrongIdFormatException()


        val vehicle = vehicleRepository.createVehicle(userId, createVehicleDTO)
        call.respond(vehicle)

    }
}