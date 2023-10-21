package rmc

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.Test
import org.junit.jupiter.api.Assertions
import rmc.db.dao.UserType
import rmc.dto.user.UserDTO
import rmc.utils.tokenManager

class EndPointTests {

    private val testUser = UserDTO(
        999,
        "email@email.com",
        UserType.STAFF,
        "Test",
        "User",
        "+316123456789",
        "Street",
        "1",
        "1234AB",
        "Breda"
    )
    private val token = tokenManager.generateJWTToken(testUser)
    val authHeader = "Bearer $token"

    @Test
    fun startTest(){
        val requests = listOf(
            mapOf("httpMethod" to "post", "endpoint" to "/user/signup"),
            mapOf("httpMethod" to "post", "endpoint" to "/user/signin"),
            mapOf("httpMethod" to "get", "endpoint" to "/user/users"),
            mapOf("httpMethod" to "get", "endpoint" to "/user/333"),
            mapOf("httpMethod" to "put", "endpoint" to "/user/333"),
            mapOf("httpMethod" to "delete", "endpoint" to "/user/333"),
            mapOf("httpMethod" to "get", "endpoint" to "/user/me"),
            mapOf("httpMethod" to "put", "endpoint" to "/user/me"),
            mapOf("httpMethod" to "post", "endpoint" to "/vehicle/createVehicle"),
            mapOf("httpMethod" to "get", "endpoint" to "/vehicle/333"),
            mapOf("httpMethod" to "get", "endpoint" to "/vehicle/all"),
            mapOf("httpMethod" to "get", "endpoint" to "/vehicle/allAvailable"),
            mapOf("httpMethod" to "get", "endpoint" to "/vehicle/user"),
            mapOf("httpMethod" to "put", "endpoint" to "/vehicle/333"),
            mapOf("httpMethod" to "put", "endpoint" to "/vehicle/setAvailability/333/true"),
            mapOf("httpMethod" to "delete", "endpoint" to "/vehicle/333"),
            mapOf("httpMethod" to "post", "endpoint" to "/rental//createRental/{vehicleId}"),
            mapOf("httpMethod" to "get", "endpoint" to "/rental/rentals"),
            mapOf("httpMethod" to "get", "endpoint" to "/rental/333"),
            mapOf("httpMethod" to "get", "endpoint" to "/rental/rentedVehicle/333"),
            mapOf("httpMethod" to "get", "endpoint" to "/rental/ownedVehicle/333"),
            mapOf("httpMethod" to "get", "endpoint" to "/rental/allRentals"),
            mapOf("httpMethod" to "get", "endpoint" to "/rental/status/333?status=denied"),
            mapOf("httpMethod" to "put", "endpoint" to "/rental/333"),
            mapOf("httpMethod" to "delete", "endpoint" to "/rental/333"),
            mapOf("httpMethod" to "delete", "endpoint" to "/rental/staff/333"),
            mapOf("httpMethod" to "delete", "endpoint" to "/user/me")
        )

        var i = 0
        for (request in requests) {
            val httpMethod = request["httpMethod"]
            val endpoint = request["endpoint"]

            if (httpMethod != null && endpoint != null) {
                testEndpoint(httpMethod, endpoint)
                i+=1
                println("======= TEST PASSED =======")
            }
        }
        println("++++++++ Total ENDPOINTS THAT PASSED TEST: $i ++++++++++")
    }


    fun testEndpoint(httpMethod: String, endpoint: String) = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = when (httpMethod) {
            "get" -> client.get(endpoint) {
                authHeader
                header(HttpHeaders.Authorization, authHeader)
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
            "post" -> client.post(endpoint) {
                authHeader
                header(HttpHeaders.Authorization, authHeader)
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())

            }
            "delete" -> client.delete(endpoint) {
                authHeader
                header(HttpHeaders.Authorization, authHeader)
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())

            }
            "put" -> client.put(endpoint) {
                authHeader
                header(HttpHeaders.Authorization, authHeader)
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())

            }
            else -> throw IllegalArgumentException("Unsupported HTTP method: $httpMethod")
        }
        println(" ### Testing $httpMethod request to $endpoint")
        println(" --FEEDBACK-- : $response")
        Assertions.assertTrue(
            response.status.value in 200..403 || response.status.value == 415 ,
            "Endpoint does not exist: ${response.status.value}"
        )
    }


}


