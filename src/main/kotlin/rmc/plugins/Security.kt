package rmc.plugins


import com.typesafe.config.ConfigFactory
import rmc.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.configureSecurity() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)

    install(Authentication) {
        jwt {

            verifier(tokenManager.verifyJWTToken())
            val realm = config.property("jwt.realm").getString()

            validate { jwtCredential ->
                if (jwtCredential.payload.getClaim("email").asString().isNotEmpty()) {
                    JWTPrincipal(jwtCredential.payload)
                } else null
            }

            challenge { _, _ ->
                call.respondText(Json.encodeToString(mapOf("Authorization" to "Bearer something.very.strange")), status = HttpStatusCode.Unauthorized)

            }

        }
    }
}
