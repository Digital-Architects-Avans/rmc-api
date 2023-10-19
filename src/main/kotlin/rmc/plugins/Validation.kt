package rmc.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import rmc.dto.user.SigninDTO
import rmc.dto.user.SignupDTO

fun Application.configureValidation() {
    install(RequestValidation) {

        validate<SignupDTO> { user ->
            try {
                user.validate()
                ValidationResult.Valid
            } catch (e: Exception) {
                ValidationResult.Invalid(e.message!!)
            }
        }

        validate<SigninDTO> { user ->
            try {
                user.validate()
                ValidationResult.Valid
            } catch (e: Exception) {
                ValidationResult.Invalid(e.message!!)
            }
        }
    }
}