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
    var name by UsersTable.name
    var phone by UsersTable.phone
    var address by UsersTable.address
}

object UsersTable : IntIdTable() {
    val email = varchar("email", 100)
    val userType = varchar("userType", 100)
    val password = varchar("password", 100)
    val name = varchar("name", 100).default("")
    val phone = varchar("phone", 100).default("")
    val address = varchar("address", 100).default("")

    init {
        index(true, email)
    }
}

fun UserEntity.toUserDto() = UserDTO(
    this.id.value,
    this.email,
    this.userType,
    this.name,
    this.phone,
    this.address
)