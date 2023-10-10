package rmc.model

import rmc.dto.user.UserDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var userType by UsersTable.userType
    var password by UsersTable.password
    var firstName by UsersTable.firstName
    var lastName by UsersTable.lastName
    var phone by UsersTable.phone
    var street by UsersTable.street
    var buildingNumber by UsersTable.buildingNumber
    var zipCode by UsersTable.zipCode
    var city by UsersTable.city
}

object UsersTable : IntIdTable() {
    val email = varchar("email", 100)
    val userType = varchar("userType", 100)
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

fun UserEntity.toUserDto() = UserDTO(
    this.id.value,
    this.email,
    this.userType,
    this.firstName,
    this.lastName,
    this.phone,
    this.street,
    this.buildingNumber,
    this.zipCode,
    this.city
)