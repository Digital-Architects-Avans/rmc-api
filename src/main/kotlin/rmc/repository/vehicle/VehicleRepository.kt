package rmc.repository.vehicle

import rmc.dto.user.UserId
import rmc.dto.vehicle.CreateVehicleDTO
import rmc.dto.vehicle.UpdateVehicleDTO
import rmc.dto.vehicle.VehicleDTO
import rmc.dto.vehicle.VehicleId

interface VehicleRepository {
    suspend fun createVehicle(userID: UserId, vehicle: CreateVehicleDTO): VehicleDTO
    suspend fun getAllVehicles(): List<VehicleDTO>
    suspend fun getAllAvailableVehicles(): List<VehicleDTO>
    suspend fun getVehicleById(vehicleId: VehicleId): VehicleDTO
    suspend fun getVehiclesByUserId(userId: UserId): List<VehicleDTO>
    suspend fun updateVehicle(vehicleId: VehicleId, vehicle: UpdateVehicleDTO)
    suspend fun setVehicleAvailability(vehicleId: VehicleId, availability: Boolean)
    suspend fun deleteVehicle(vehicleId: VehicleId)
}