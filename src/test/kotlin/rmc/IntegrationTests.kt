package rmc

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import rmc.db.dao.EngineType
import rmc.db.dao.UserType
import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.RentalDTO
import rmc.dto.user.SignupDTO
import rmc.dto.user.UserDTO
import rmc.dto.vehicle.CreateVehicleDTO
import rmc.dto.vehicle.VehicleDTO
import rmc.repository.rental.rentalRepository
import rmc.repository.user.userRepository
import rmc.repository.vehicle.vehicleRepository
import rmc.utils.tokenManager
import kotlinx.datetime.LocalDate
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class IntegrationTests {
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

    private val testVehicle = CreateVehicleDTO(
        "Tesla",
        "Model S",
        2021,
        "Full size luxury sedan",
        EngineType.BEV,
        generateRandomLicensePlate(),
        "https://www.imageoftesla.com/image1.png",
        51.584009.toFloat(),
        4.795735.toFloat(),
        129.00,
        true
    )

    private val testRental = CreateRentalDTO(
        generateRandomDay()
    )

    // Function that returns a random day to be used to create a rental test
    private fun generateRandomDay(): LocalDate {
        val randomDay = Random.nextInt(1, 30) // 1 to 29 (inclusive)
        val randomMonth = Random.nextInt(1, 13) // 1 to 12
        val randomYear = Random.nextInt(1970, 2024) // 1970 to 2023

        return LocalDate(randomYear, randomMonth, randomDay)
    }

    // Function that returns a random license plate, iot prevent a duplicate unique index key insert
    private fun generateRandomLicensePlate(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..6)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
            .chunked(2).joinToString("-")
    }

    /* IT-001 Integration Test Testcase: Endpoint /user/signup
    - Test if JWT token is returned
    - Test if JWT token satisfies to JWT regex
    - Test if HttpStatusCode.Created is returned
    - Test if returned user equals the user stored in the database */
    @Test
    fun `sign user up`() = testApplication {
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
        val returnedUser = Json.decodeFromString<UserDTO>(response.bodyAsText())
        val userFromDb = userRepository.getUserById(returnedUser.id)

        assert(returnedToken.matches(jwtRegex))
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(userFromDb, returnedUser)
    }

    /* IT-002 Integration Test Testcase: Endpoint /vehicle/createVehicle
    // Test if HttpStatusCode.Created is returned
    // Test if vehicle returned equals vehicle stored in the database */
    @Test
    fun `create vehicle for user from JWT`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/vehicle/createVehicle") {
            setBody(Json.encodeToString(testVehicle))
            val authHeader = "Bearer $token"
            header(HttpHeaders.Authorization, authHeader)
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }

        val vehicleFromResponse = Json.decodeFromString<VehicleDTO>(response.bodyAsText())
        val vehicleFromDb = vehicleRepository.getVehicleById(vehicleFromResponse.id)

        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(vehicleFromDb, vehicleFromResponse)
    }

    /* IT-003 Integration Test Testcase: Endpoint /rental/createRental
    // Test if HttpStatusCode.Created is returned
    // Test if rental returned equals the rental stored in the database */
    @Test
    fun `create rental for vehicle for user from JWT`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rental/createRental/1") {
            setBody(Json.encodeToString(testRental))
            val authHeader = "Bearer $token"
            header(HttpHeaders.Authorization, authHeader)
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }

        val rentalFromResponse = Json.decodeFromString<RentalDTO>(response.bodyAsText())
        val rentalFromDb = rentalRepository.getRentalById(rentalFromResponse.id)

        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(rentalFromDb, rentalFromResponse)
    }
}
