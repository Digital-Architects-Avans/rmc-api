package rmc.repository.rental

import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.RentalDTO
import rmc.dto.rental.RentalId
import rmc.dto.rental.UpdateRentalDTO
import rmc.dto.user.*
import rmc.dto.vehicle.VehicleId

interface RentalRepository {
    suspend fun allRentals(): List<RentalDTO>
    suspend fun getRentalById(rentalId: RentalId): RentalDTO
    suspend fun createRental(userId: UserId, vehicleId: VehicleId, rental: CreateRentalDTO): RentalDTO
    suspend fun updateRental(rentalId: RentalId, rental: UpdateRentalDTO)
    suspend fun deleteRental(rentalId: RentalId)

}