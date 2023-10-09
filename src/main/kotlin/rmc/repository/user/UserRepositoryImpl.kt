package rmc.repository.user

import rmc.dto.user.*
import rmc.error.AuthenticationFailed
import rmc.error.EntityNotFound
import rmc.model.UserEntity
import rmc.model.UsersTable
import rmc.model.toUserDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import rmc.db.DatabaseFactory.dbQuery
import rmc.dto.user.*
import rmc.repository.user.UserRepository

class UserRepositoryImpl : UserRepository {
    override suspend fun allUsers(): List<UserDTO> = dbQuery {
        UserEntity.all().map {
            it.toUserDto()
        }
    }

    override suspend fun getUserById(id: UserId): UserDTO = dbQuery {
        UserEntity.findById(id)?.toUserDto() ?: throw EntityNotFound("User")
    }

    override suspend fun createUser(user: SignupDTO): UserDTO = dbQuery {
        UserEntity.new {
            email = user.email
            userType = user.userType
            password = user.hashedPassword()
        }.toUserDto()
    }

    override suspend fun updateUser(id: UserId, user: UpdateUserDTO) = dbQuery {
        UserEntity.findById(id)?.let {
            it.password = user.hashedPassword()
            it.userType = user.userType
            it.name = user.name
            it.phone = user.phone
            it.address = user.address
        } ?: throw EntityNotFound("User")
    }

    override suspend fun deleteUser(id: UserId) = dbQuery {
        UserEntity.findById(id)?.delete() ?: throw EntityNotFound("User")
    }

    override suspend fun authenticateUser(user: SigninDTO): UserDTO {
        val userEntity = dbQuery {
            UserEntity.find { UsersTable.email eq user.email }.firstOrNull() ?: throw AuthenticationFailed()
        }
        if (!BCrypt.checkpw(user.password, userEntity.password)) throw AuthenticationFailed()
        return userEntity.toUserDto()
    }
}