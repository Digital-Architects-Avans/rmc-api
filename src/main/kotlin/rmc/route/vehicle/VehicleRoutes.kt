package rmc.route.vehicle

import rmc.error.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rmc.dao.vehicle.VehicleRepositoryImpl
import rmc.dto.vehicle.CreateVehicleDTO

fun Route.vehicleRoutes() {
    val vehicleRepository = VehicleRepositoryImpl()

//    authenticate {
        post("createVehicle") {
            val createVehicleDTO = call.receive<CreateVehicleDTO>()
//            val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
//            val userId = principal.payload.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()
            val userId = 1
            val vehicle = vehicleRepository.createVehicle(userId, createVehicleDTO)
            call.respond(vehicle)
        }
//    }
}