package rmc.repository.user

import rmc.dto.user.*

interface UserRepository {
    suspend fun allUsers(): List<UserDTO>
    suspend fun getUserById(userId: UserId): UserDTO
    suspend fun createUser(user: SignupDTO): UserDTO
    suspend fun updateUser(userId: UserId, user: UpdateUserDTO)
    suspend fun deleteUser(userId: UserId)
    suspend fun authenticateUser(user: SigninDTO): UserDTO
}