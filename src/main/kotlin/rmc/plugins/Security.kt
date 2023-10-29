package rmc.plugins

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.httpsredirect.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import org.slf4j.event.Level
import rmc.error.AuthenticationFailed
import rmc.utils.TokenManager
import kotlin.time.Duration.Companion.seconds

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
                throw AuthenticationFailed()
            }
        }
    }

    install(CallLogging) {
    level = Level.TRACE
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }

    // Set a limit per request per requestKey, uses the client address as requestKey
    install(RateLimit) {
        register {
            rateLimiter(limit = 10, refillPeriod = 60.seconds)
            requestKey { call -> call.request.origin.remoteHost }
        }
    }

    install(HttpsRedirect) {
        sslPort = 8443
        permanentRedirect = true
    }
}
