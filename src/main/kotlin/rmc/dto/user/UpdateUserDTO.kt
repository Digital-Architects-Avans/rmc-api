package rmc.dto.user

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt
import rmc.model.UserType

@Serializable
data class UpdateUserDTO (
    val email: String,
    val userType: UserType,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val street: String,
    val buildingNumber: String,
    val zipCode: String,
    val city: String
) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}