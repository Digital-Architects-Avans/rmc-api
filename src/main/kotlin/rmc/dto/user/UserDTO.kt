package rmc.dto.user

import kotlinx.serialization.Serializable
import rmc.db.entity.UserType

typealias UserId = Int

@Serializable
data class UserDTO (
    val id: Int,
    val email: String,
    val userType: UserType,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val street: String,
    val buildingNumber: String,
    val zipCode: String,
    val city: String
)