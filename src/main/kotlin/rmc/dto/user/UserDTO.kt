package rmc.dto.user

import kotlinx.serialization.Serializable

typealias UserId = Int

@Serializable
data class UserDTO (
    val id: Int,
    val email: String,
    val userType: String,
    val name: String,
    val phone: String,
    val address: String
)