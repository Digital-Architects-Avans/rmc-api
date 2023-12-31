package rmc.dto.user

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt
import rmc.db.dao.UserType
import rmc.error.WrongFormat

@Serializable
data class SignupDTO (
    val email: String,
    val userType: UserType,
    val password: String
) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun validate() {
        val emailRegex = Regex(pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

        // Password regex 6 char + 1 digit
        //val passwordRegex = Regex(pattern = "^(?=.+[A-Za-z])(?=.+\\d)[A-Za-z\\d]{6,}\$")

        // Password regex
        // - At least one upper case English letter, (?=.*?[A-Z])
        // - At least one lower case English letter, (?=.*?[a-z])
        // - At least one digit, (?=.*?[0-9])
        // - At least one special character, (?=.*?[#?!@$%^&*-])
        // - Minimum eight in length .{8,} (with the anchors)
        val passwordRegex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")

        if (!email.matches(emailRegex)) throw WrongFormat("Email")
        if (!password.matches(passwordRegex)) throw WrongFormat("Password")
    }

}