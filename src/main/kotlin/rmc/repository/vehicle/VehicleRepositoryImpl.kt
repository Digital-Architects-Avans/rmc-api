package rmc.repository.vehicle

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import rmc.db.DatabaseFactory.dbQuery
import rmc.db.dao.UserEntity
import rmc.db.dao.VehicleEntity
import rmc.db.dao.toVehicleDTO
import rmc.db.tables.UsersTable
import rmc.db.tables.VehiclesTable
import rmc.dto.user.UserId
import rmc.dto.vehicle.CreateVehicleDTO
import rmc.dto.vehicle.UpdateVehicleDTO
import rmc.dto.vehicle.VehicleDTO
import rmc.dto.vehicle.VehicleId
import rmc.error.EntityWithIdNotFound
import rmc.error.VehicleAlreadyRegistered

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
            longitude = vehicle.longitude
            latitude = vehicle.latitude
            price = vehicle.price
            availability = vehicle.availability
        }.toVehicleDTO()
    }

    override suspend fun getVehicleById(vehicleId: VehicleId): VehicleDTO = dbQuery{
        VehicleEntity.findById(vehicleId)?.toVehicleDTO() ?: throw EntityWithIdNotFound("vehicle", vehicleId)
    }

    override suspend fun getAllVehicles(): List<VehicleDTO> = dbQuery{
        VehicleEntity.all().map {
            it.toVehicleDTO()
        }
    }

    override suspend fun getVehiclesByUserId(userId: VehicleId): List<VehicleDTO> {
        val vehicleEntities = dbQuery {
            VehicleEntity.find(VehiclesTable.userId eq userId).toList()
        }

        if (vehicleEntities.isEmpty()) throw EntityWithIdNotFound("vehicle", userId)
        return vehicleEntities.map { it.toVehicleDTO() }
    }

    override suspend fun updateVehicle(vehicleId: VehicleId, vehicle: UpdateVehicleDTO) = dbQuery {
        VehicleEntity.findById(vehicleId)?.let {
            it.brand = vehicle.brand
            it.model = vehicle.model
            it.year = vehicle.year
            it.vehicleClass = vehicle.vehicleClass
            it.engineType = vehicle.engineType
            it.licensePlate = vehicle.licensePlate
            it.imgLink = vehicle.imgLink
            it.latitude = vehicle.latitude
            it.longitude = vehicle.longitude
            it.price = vehicle.price
            it.availability = vehicle.availability
        } ?: throw EntityWithIdNotFound("vehicle", vehicleId)
    }

    override suspend fun deleteVehicle(vehicleId: VehicleId) = dbQuery {
        VehicleEntity.findById(vehicleId)?.delete() ?: throw EntityWithIdNotFound("vehicle", vehicleId)
    }
}
val vehicleRepository: VehicleRepository = VehicleRepositoryImpl()

