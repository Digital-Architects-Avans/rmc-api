package rmc.dao.user

import rmc.dto.user.*
import rmc.error.AuthenticationFailed
import rmc.error.EntityWithIdNotFound
import rmc.db.entity.UserEntity
import rmc.db.tables.UsersTable
import rmc.db.entity.toUserDto
import org.mindrot.jbcrypt.BCrypt
import rmc.db.DatabaseFactory.dbQuery

class UserRepositoryImpl : UserRepository {
    override suspend fun allUsers(): List<UserDTO> = dbQuery {
        UserEntity.all().map {
            it.toUserDto()
        }
    }

    override suspend fun getUserById(userId: UserId): UserDTO = dbQuery {
        UserEntity.findById(userId)?.toUserDto() ?: throw EntityWithIdNotFound("User", userId)
    }

    override suspend fun createUser(user: SignupDTO): UserDTO = dbQuery {
        UserEntity.new {
            email = user.email
            userType = user.userType
            password = user.hashedPassword()
        }.toUserDto()
    }

    override suspend fun updateUser(userId: UserId, user: UpdateUserDTO) = dbQuery {
        UserEntity.findById(userId)?.let {
            it.password = user.hashedPassword()
            it.userType = user.userType
            it.firstName = user.firstName
            it.lastName = user.lastName
            it.phone = user.phone
            it.street = user.street
            it.buildingNumber = user.buildingNumber
            it.zipCode = user.zipCode
            it.city = user.city
        } ?: throw EntityWithIdNotFound("User", userId)
    }

    override suspend fun deleteUser(userId: UserId) = dbQuery {
        UserEntity.findById(userId)?.delete() ?: throw EntityWithIdNotFound("User", userId)
    }

    override suspend fun authenticateUser(user: SigninDTO): UserDTO {
        val userEntity = dbQuery {
            UserEntity.find { UsersTable.email eq user.email }.firstOrNull() ?: throw AuthenticationFailed()
        }
        if (!BCrypt.checkpw(user.password, userEntity.password)) throw AuthenticationFailed()
        return userEntity.toUserDto()
    }
}