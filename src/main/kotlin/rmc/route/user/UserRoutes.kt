package rmc.route.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import rmc.db.dao.UserType
import rmc.dto.user.SigninDTO
import rmc.dto.user.SignupDTO
import rmc.dto.user.UpdateUserDTO
import rmc.error.*
import rmc.plugins.authorize
import rmc.plugins.currentUserId
import rmc.repository.user.userRepository
import rmc.utils.tokenManager


fun Route.userRoutes() {

    route("/user") {
        rateLimit {
            post("/signup") {
                val signUpDTO = call.receive<SignupDTO>()

                try {
                    val newUser = userRepository.createUser(signUpDTO)
                    val token = tokenManager.generateJWTToken(newUser)

                    call.response.header("Authorization", token)
                    call.respondText(Json.encodeToString(newUser), status = HttpStatusCode.Created)
                } catch (e: Exception) {
                    throw EmailAlreadyRegistered()
                }
            }

            post("/signin") {
                val signInDTO = call.receive<SigninDTO>()

                try {
                    val authenticatedUser = userRepository.authenticateUser(signInDTO)
                    val token = tokenManager.generateJWTToken(authenticatedUser)

                    call.response.header("Authorization", token)
                    call.respondText(
                        "Welcome, ${authenticatedUser.email}, you have been successfully logged in.",
                        status = HttpStatusCode.OK
                    )
                } catch (e: Exception) {
                    throw AuthenticationFailed()
                }
            }

            authenticate {
                get("/users") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.STAFF, user)

                    val users = userRepository.allUsers()
                    call.respond(users)
                }

                get("/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.STAFF, user)

                    val id = call.parameters["id"]?.toInt() ?: throw WrongFormat("id")
                    val found = userRepository.getUserById(id)
                    found.let { call.respond(it) }
                }

                put("/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.STAFF, user)

                    val id = call.parameters["id"]?.toInt() ?: throw throw WrongFormat("id")
                    val updateUserDTO = call.receive<UpdateUserDTO>()

                    try {
                        val existingUser = userRepository.getUserById(id)
                        if (existingUser.email != updateUserDTO.email) throw EmailUpdateAttemptError()
                        userRepository.updateUser(id, updateUserDTO)
                        val updatedUser = userRepository.getUserById(id)
                        call.respond(updatedUser)
                    } catch (e: EntityWithIdNotFound) {
                        val signUpDto = SignupDTO(updateUserDTO.email, updateUserDTO.userType, updateUserDTO.password)
                        val newUser = userRepository.createUser(signUpDto)
                        call.respond(newUser)
                    }
                }

                delete("/{id}") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.STAFF, user)

                    val id = call.parameters["id"]?.toInt() ?: throw throw WrongFormat("id")
                    call.respond(userRepository.deleteUser(id))
                }

                get("/me") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    call.respond(user)
                }

                put("/me") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    val updateUserDTO = call.receive<UpdateUserDTO>()

                    try {
                        val existingUser = userRepository.getUserById(userId)
                        if (existingUser.email != updateUserDTO.email) throw EmailUpdateAttemptError()
                        userRepository.updateUser(userId, updateUserDTO)
                        val updatedUser = userRepository.getUserById(userId)
                        call.respond(updatedUser)
                    } catch (e: EntityWithIdNotFound) {
                        val signUpDto = SignupDTO(updateUserDTO.email, updateUserDTO.userType, updateUserDTO.password)
                        val newUser = userRepository.createUser(signUpDto)
                        call.respond(newUser)
                    }
                }

                delete("/me") {
                    val userId = currentUserId()
                    val user = userRepository.getUserById(userId)
                    authorize(UserType.CLIENT, user)

                    call.respond(userRepository.deleteUser(userId))
                }
            }
        }
    }
}
