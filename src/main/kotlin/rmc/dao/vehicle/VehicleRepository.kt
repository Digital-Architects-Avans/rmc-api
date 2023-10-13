package rmc.dao.vehicle

import rmc.dto.vehicle.CreateVehicleDTO
import rmc.dto.vehicle.VehicleDTO
import rmc.dto.user.UserId

interface VehicleRepository {
    suspend fun createVehicle(userID: UserId, vehicle: CreateVehicleDTO): VehicleDTO
}