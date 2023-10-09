package rmc.repository.user

import rmc.dto.user.*

interface UserRepository {
    suspend fun allUsers(): List<UserDTO>
    suspend fun getUserById(id: UserId): UserDTO
    suspend fun createUser(user: SignupDTO): UserDTO
    suspend fun updateUser(id: UserId, user: UpdateUserDTO)
    suspend fun deleteUser(id: UserId)
    suspend fun authenticateUser(user: SigninDTO): UserDTO
}