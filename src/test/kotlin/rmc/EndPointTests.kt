package rmc

import org.junit.Test
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import rmc.db.dao.UserType
import rmc.dto.user.SignupDTO
import rmc.dto.user.UserDTO
import rmc.utils.tokenManager
import kotlin.test.fail

class EndPointTests {

    private val jwtRegex = Regex("^[a-zA-Z0-9]+?\\.[a-zA-Z0-9]+?\\..+")
    private val time = System.currentTimeMillis()

    private val userCredentials = SignupDTO(
        "$time@email.com",
        UserType.CLIENT,
        "StrongPassword$time!"
    )

    private val testUser = UserDTO(
        1,
        "email@email.com",
        UserType.CLIENT,
        "Test",
        "User",
        "+316123456789",
        "Street",
        "1",
        "1234AB",
        "Breda"
    )

    private val token = tokenManager.generateJWTToken(testUser)
    @Test
   fun testAPI() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/user/signup") {
            setBody(Json.encodeToString(userCredentials))
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }

        val authorizationHeader = response.headers["Authorization"]
        val returnedToken =
            authorizationHeader?.substringAfter("Bearer $token")
                ?: fail("No token returned in the Authorization header")
   }

}
