package rmc.dto.user

import kotlinx.serialization.Serializable
import rmc.error.WrongEmailFormat

@Serializable
data class SigninDTO (
    val email: String,
    val password: String
) {
    fun validate() {
        val emailRegex = Regex(pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        if (!email.matches(emailRegex)) throw WrongEmailFormat()
    }
}