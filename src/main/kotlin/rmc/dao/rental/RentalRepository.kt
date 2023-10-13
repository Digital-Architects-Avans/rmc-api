package rmc.dao.rental

import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.RentalDTO
import rmc.dto.user.UserId

interface RentalRepository {
    suspend fun createRental(vehicleId: Int, userId: UserId, rental: CreateRentalDTO): RentalDTO

}