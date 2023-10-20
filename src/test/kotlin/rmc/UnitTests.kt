package rmc

import org.junit.Test
import rmc.db.dao.UserType
import rmc.dto.user.SignupDTO
import rmc.error.WrongFormat
import kotlin.test.fail


class SignupDTOTest {

    //UT-001 Unittest Testcase: testValidInput
    @Test
    fun testValidInput() {

        val validSignupDTO = SignupDTO(
            email = "test@example.com",
            userType = UserType.CLIENT,
            password = "Password1#"
        )

        try {
            validSignupDTO.validate()
            println("Test 'testValidInput' passed successfully!")
        } catch (e: Exception) {
            fail("Test 'testValidInput' failed: ${e.message}")
        }
    }

    //UT-002 Unittest Testcase: testInValidEmail
    @Test
    fun testInvalidEmail() {

        val invalidSignupDTO = SignupDTO(
            email = "invalid_email",
            userType = UserType.CLIENT,
            password = "Password1#"
        )

        try {
            invalidSignupDTO.validate()
            fail("Test 'testInvalidEmail' failed: No exception thrown")
        } catch (e: WrongFormat) {
            println("Test 'testInvalidEmail' passed successfully!")
        }
    }


    //UT-003 Unittest Testcase: testInValidPassword
    @Test
    fun testInvalidPassword() {

        val invalidSignupDTO = SignupDTO(
            email = "test@example.com",
            userType = UserType.CLIENT,
            password = "weak"
        )

        try {
            invalidSignupDTO.validate()
            fail("Test 'testInvalidPassword' failed: No exception thrown")
        } catch (e: WrongFormat) {
            println("Test 'testInvalidPassword' passed successfully!")
        }
    }

}

