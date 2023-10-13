package rmc.db

import org.jetbrains.exposed.dao.id.IntIdTable
import rmc.model.UserType

object UsersTable : IntIdTable() {
    val email = varchar("email", 100)
    val userType = enumerationByName<UserType>("userType", 100)
    val password = varchar("password", 100)
    val firstName = varchar("firstName", 100).default("")
    val lastName = varchar("lastName", 100).default("")
    val phone = varchar("phone", 100).default("")
    val street = varchar("street", 100).default("")
    val buildingNumber = varchar("buildingNumber", 100).default("")
    val zipCode = varchar("zipCode", 100).default("")
    val city = varchar("city", 100).default("")

    init {
        index(true, email)
    }
}