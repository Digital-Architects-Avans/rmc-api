package rmc.repository.rental

import kotlinx.datetime.LocalDate
import rmc.db.dao.RentalStatus
import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.RentalDTO
import rmc.dto.rental.RentalId
import rmc.dto.rental.UpdateRentalDTO
import rmc.dto.user.UserId
import rmc.dto.vehicle.VehicleId

interface RentalRepository {
    suspend fun allRentals(): List<RentalDTO>
    suspend fun getRentalById(rentalId: RentalId): RentalDTO
    suspend fun getRentalByUserId(userId: UserId): List<RentalDTO>
    suspend fun getRentalByVehicleId(vehicleId: VehicleId): List<RentalDTO>
    suspend fun createRental(userId: UserId, vehicleId: VehicleId, rental: CreateRentalDTO): RentalDTO
    suspend fun updateRental(rentalId: RentalId, rental: UpdateRentalDTO)
    suspend fun updateRentalStatus(rentalId: RentalId, status: RentalStatus)
    suspend fun cascadeRentalStatus(approvedRental: RentalId, vehicleId: VehicleId, date: LocalDate)
    suspend fun deleteRental(rentalId: RentalId)

}