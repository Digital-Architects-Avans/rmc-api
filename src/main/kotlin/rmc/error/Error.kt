package rmc.error

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import rmc.db.dao.UserType
import rmc.dto.user.UserId
import rmc.dto.vehicle.VehicleId

open class EntityWithIdNotFound(entity: String, id: Int) : Exception("""{"Exception":"$entity with id $id not found"}""")
class InvalidEmailError : Exception("""{"Exception":"Invalid email"}""")
class InvalidPasswordError : Exception("""{"Exception":"Invalid password"}""")
class AuthenticationFailed : Exception("""{"Exception":"Authentication Failed"}""")
class EmailAlreadyRegistered : Exception("""{"Exception":"Email already registered"}""")
class VehicleAlreadyRegistered : Exception("""{"Exception":"Vehicle already registered"}""")
class EmailUpdateAttemptError : Exception("""{"Exception":"Email update not allowed"}""")
class WrongFormat(type: String) : Exception("""{"Exception":"Wrong $type format"}""")
class WrongIdRangeException : Exception("""{"Exception":"Wrong ID range"}""")
class WrongUserType : Exception("""{"Exception":"Access denied"}""")
class MissingPermissionError(requiredUserType: UserType) : Exception("""{"Exception":"User is not $requiredUserType"}""")
class NotOwnerOfEntityWithId(entity: String, id: Int) : Exception("""{"Exception":"You are not the owner of $entity with id $id"}""")
class StatusNotFound : Exception("""{"Exception":"Status not found"}""")
class VehicleNotAvailable(vehicleId: VehicleId) : Exception("""{"Exception":"Vehicle with id $vehicleId is not available"}""")
class VehicleIsAlreadyRented(vehicleId: VehicleId, date: LocalDate) : Exception("""{"Exception":"Vehicle with id $vehicleId is already rented by another user on $date"}""")
class NoRentalsForUserFound(userId: UserId) : Exception("""{"Exception":"No rentals for user with $userId found"}""")
class NoVehiclesForUserFound(userId: UserId) : Exception("""{"Exception":"No vehicles for user with $userId found"}""")


@OptIn(ExperimentalSerializationApi::class)
fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Unauthorized)
        }
        exception<InvalidEmailError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<InvalidPasswordError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<AuthenticationFailed> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Unauthorized)
        }
        exception<EmailAlreadyRegistered> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<VehicleAlreadyRegistered> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<EmailUpdateAttemptError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<WrongFormat> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<WrongIdRangeException> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<WrongUserType> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<EntityWithIdNotFound> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<MissingFieldException> { call, cause ->
            call.respondText(cause.message ?: "Some fields are missing", ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<MissingPermissionError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        status(HttpStatusCode.TooManyRequests) { call, status ->
            val retryAfter = call.response.headers["Retry-After"]
            call.respondText(text = "429: Too many requests. Wait for $retryAfter seconds.", status = status)
        }
        exception<NotOwnerOfEntityWithId> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<StatusNotFound> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<VehicleNotAvailable> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<VehicleIsAlreadyRented> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Forbidden)
        }
        exception<NoRentalsForUserFound> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.NotFound)
        }
        exception<NoVehiclesForUserFound> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.NotFound)
        }
    }
}
