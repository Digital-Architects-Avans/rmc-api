package rmc.dto.user

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UpdateUserDTO (
    val email: String,
    val userType: String,
    val password: String,
    val name: String,
    val phone: String,
    val address: String
) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}