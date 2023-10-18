package rmc.error

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import rmc.db.dao.UserType

open class EntityWithIdNotFound(entity: String, id: Int) : Exception("""{"status":"$entity with id $id not found"}""")
class InvalidEmailError : Exception("""{"status":"Invalid email"}""")
class InvalidPasswordError : Exception("""{"status":"Invalid password"}""")
class AuthenticationFailed : Exception("""{"status":"Authentication Failed"}""")
class EmailAlreadyRegistered : Exception("""{"status":"Email already registered"}""")
class VehicleAlreadyRegistered : Exception("""{"status":"Vehicle already registered"}""")
class EmailUpdateAttemptError : Exception("""{"status":"Email update not allowed"}""")
class WrongIdFormatException : Exception("""{"status":"Wrong ID format"}""")
class WrongIdRangeException : Exception("""{"status":"Wrong ID range"}""")
class WrongEmailFormat : Exception("""{"status":"Wrong email format"}""")
class WrongPasswordFormat : Exception("""{"status":"Wrong password format"}""")
class BearerStrangeError : Exception("""{"Authorization":"Bearer something.very.strange"}""")
class WrongUserType : Exception("""{"status":"Access denied"}""")
class MissingPermissionError(requiredUserType: UserType) : Exception("""{"status":"User is not $requiredUserType"}""")
class NotOwnerOfEntityWithId(entity: String, id: Int) : Exception("""{"status":"You are not the owner of $entity with id $id"}""")
class StatusNotFound : Exception("""{"status":"Status not found"}""")


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
        exception<WrongIdFormatException> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<WrongIdRangeException> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<WrongPasswordFormat> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
        exception<BearerStrangeError> { call, cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.Unauthorized)
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
    }
}
