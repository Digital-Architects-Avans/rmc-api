package rmc.dao.rental

import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.RentalDTO
import rmc.dto.user.UserId
import rmc.dto.vehicle.VehicleId

interface RentalRepository {
    suspend fun createRental(userId: UserId, vehicleId: VehicleId, rental: CreateRentalDTO): RentalDTO

}