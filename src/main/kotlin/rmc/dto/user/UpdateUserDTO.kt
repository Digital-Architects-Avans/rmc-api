package rmc.dto.user

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UpdateUserDTO (
    val email: String,
    val userType: String,
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