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
        route("/vehicle") {
            post("/createVehicle") {
                val createVehicleDTO = call.receive<CreateVehicleDTO>()
//                val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
//                val userId = principal.payload.getClaim("userId")?.asInt() ?: throw AuthenticationFailed()
                val userId = 1
                val vehicle = vehicleRepository.createVehicle(userId, createVehicleDTO)
                call.respond(vehicle)
            }

            get("/{id}") {
//                val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
//                val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()

//                if (userType != "STAFF") throw WrongUserType()
                val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                val found = vehicleRepository.getVehicleById(id)
                found.let { call.respond(it) }
            }

            get("/all") {
//                val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
//                val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()
//
//                if (userType != "STAFF") throw WrongUserType()
                val vehicles = vehicleRepository.getAllVehicles()
                call.respond(vehicles)
            }

            get("/user{id}") {
//                val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationFailed()
//                val userType = principal.payload.getClaim("userType")?.asString() ?: throw AuthenticationFailed()

//                if (userType != "STAFF") throw WrongUserType()
                val id = call.parameters["id"]?.toInt() ?: throw WrongIdFormatException()
                val found = vehicleRepository.getVehiclesByUserId(id)
                found.let { call.respond(it) }
            }

            put (){

                TODO()

            }

            delete() {

                TODO()

            }
        }
    }
