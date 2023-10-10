package rmc.dto.user

import kotlinx.serialization.Serializable

typealias UserId = Int

@Serializable
data class UserDTO (
    val id: Int,
    val email: String,
    val userType: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val street: String,
    val buildingNumber: String,
    val zipCode: String,
    val city: String
)