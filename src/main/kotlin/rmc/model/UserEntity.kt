package rmc.model

import rmc.dto.user.UserDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import rmc.db.UsersTable

enum class UserType {
    STAFF, CLIENT, OTHER
}

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