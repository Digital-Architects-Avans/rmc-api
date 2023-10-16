package rmc

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rmc.db.dao.UserType
import rmc.dto.user.SignupDTO
import kotlin.test.*


@Serializable
data class Token(val token: String)

class ApplicationTest {
    @Test
    fun testPostUserSignup() = testApplication {
        val jwtRegex = """^[a-zA-Z0-9]+?\.[a-zA-Z0-9]+?\..+""".toRegex()
        val time = System.currentTimeMillis()
        val userCredentials = SignupDTO("$time@staff.com", UserType.OTHER, "StrongPassword$time!")

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/user/signup") {
            contentType(ContentType.Application.Json)
            setBody(userCredentials)
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }

        val principal = Json.decodeFromString<Token>(response.bodyAsText())

        assert(principal.token.matches(jwtRegex))
        assertEquals("""{"token":"${principal.token}"}""", response.bodyAsText())
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testPostVehicleCreatevehicle() = testApplication {
        application {
            module()
        }
        client.post("/vehicle/createVehicle").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostRentalCreaterentalVehicleid() = testApplication {
        application {
            module()
        }
        client.post("/rental/createRental/{vehicleId}").apply {
            TODO("Please write your test here")
        }
    }
}

