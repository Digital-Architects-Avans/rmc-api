package rmc

import org.junit.Test
import rmc.db.dao.UserType
import rmc.dto.user.SignupDTO
import rmc.error.WrongFormat
import kotlin.test.fail


import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rmc.dto.vehicle.CreateVehicleDTO
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
        val vehicle = CreateVehicleDTO()

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/vehicle/createVehicle") {
            contentType(ContentType.Application.Json)
            setBody(vehicle)
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }

//        assert(principal.token.matches(jwtRegex))
//        assertEquals("""{"token":"${principal.token}"}""", response.bodyAsText())
//        assertEquals(HttpStatusCode.Created, response.status)
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


class SignupDTOTest {

    //UT-001 Unittest Testcase: testValidInput
    @Test
    fun testValidInput() {

        val validSignupDTO = SignupDTO(
            email = "test@example.com",
            userType = UserType.CLIENT,
            password = "Password1#"
        )

        try {
            validSignupDTO.validate()
            println("Test 'testValidInput' passed successfully!")
        } catch (e: Exception) {
            fail("Test 'testValidInput' failed: ${e.message}")
        }
    }

    //UT-002 Unittest Testcase: testInValidEmail
    @Test
    fun testInvalidEmail() {

        val invalidSignupDTO = SignupDTO(
            email = "invalid_email",
            userType = UserType.CLIENT,
            password = "Password1#"
        )

        try {
            invalidSignupDTO.validate()
            fail("Test 'testInvalidEmail' failed: No exception thrown")
        } catch (e: WrongFormat) {
            println("Test 'testInvalidEmail' passed successfully!")
        }
    }


    //UT-003 Unittest Testcase: testInValidPassword
    @Test
    fun testInvalidPassword() {

        val invalidSignupDTO = SignupDTO(
            email = "test@example.com",
            userType = UserType.CLIENT,
            password = "weak"
        )

        try {
            invalidSignupDTO.validate()
            fail("Test 'testInvalidPassword' failed: No exception thrown")
        } catch (e: WrongFormat) {
            println("Test 'testInvalidPassword' passed successfully!")
        }
    }

}

