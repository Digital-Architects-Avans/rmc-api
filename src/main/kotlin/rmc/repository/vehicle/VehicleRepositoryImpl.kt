package rmc.dao.vehicle

import org.jetbrains.exposed.dao.id.EntityID
import rmc.db.DatabaseFactory.dbQuery
import rmc.db.tables.UsersTable
import rmc.db.tables.VehiclesTable
import rmc.dto.user.UserId
import rmc.dto.vehicle.CreateVehicleDTO
import rmc.dto.vehicle.VehicleDTO
import rmc.error.EntityWithIdNotFound
import rmc.error.VehicleAlreadyRegistered
import rmc.dto.vehicle.UpdateVehicleDTO
import rmc.dto.vehicle.VehicleId
import rmc.db.dao.UserEntity
import rmc.db.dao.VehicleEntity
import rmc.db.dao.toVehicleDTO
import rmc.repository.vehicle.VehicleRepository

class VehicleRepositoryImpl : VehicleRepository {
    override suspend fun createVehicle(userID: UserId, vehicle: CreateVehicleDTO): VehicleDTO = dbQuery {
        UserEntity.findById(userID) ?: throw EntityWithIdNotFound("User", userID)
        val checkForVehicle = VehicleEntity.find { VehiclesTable.licensePlate eq vehicle.licensePlate }.toList()
        if (checkForVehicle.isNotEmpty()) throw VehicleAlreadyRegistered()

        VehicleEntity.new {
            userId = EntityID(userID, UsersTable)
            brand = vehicle.brand
            model = vehicle.model
            year = vehicle.year
            vehicleClass = vehicle.vehicleClass
            engineType = vehicle.engineType
            licensePlate = vehicle.licensePlate
            imgLink = vehicle.imgLink
            vehicleLocation = vehicle.vehicleLocation
            price = vehicle.price
            availability = vehicle.availability
        }.toVehicleDTO()
    }

    override suspend fun getVehicleByID( vehicleId: VehicleId ){
        TODO("Not yet implemented")
    }

    override suspend fun getAllVehicles(): List<VehicleDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getVehicleByUserId(userID: UserId): List<VehicleDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun updateVehicle(vehicleId: VehicleId, vehicle: UpdateVehicleDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVehicle(vehicleId: VehicleId) {
        TODO("Not yet implemented")
    }
}
