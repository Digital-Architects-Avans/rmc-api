package rmc.dao.vehicle

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import rmc.db.DatabaseFactory.dbQuery
import rmc.db.tables.UsersTable
import rmc.db.tables.VehiclesTable
import rmc.dto.user.UserId
import rmc.dto.vehicle.CreateVehicleDTO
import rmc.dto.vehicle.VehicleDTO
import rmc.error.EntityWithIdNotFound
import rmc.error.VehicleAlreadyRegistered
import rmc.db.entity.UserEntity
import rmc.db.entity.VehicleEntity
import rmc.db.entity.toUserDto
import rmc.db.entity.toVehicleDTO
import rmc.dto.vehicle.UpdateVehicleDTO
import rmc.dto.vehicle.VehicleId

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

    override suspend fun getVehicleById( vehicleId: VehicleId ): VehicleDTO = dbQuery{
        VehicleEntity.findById(vehicleId)?.toVehicleDTO() ?: throw EntityWithIdNotFound("Vehicle", vehicleId)
    }

    override suspend fun getAllVehicles(): List<VehicleDTO> = dbQuery{
        VehicleEntity.all().map {
            it.toVehicleDTO()
        }
    }

//    override suspend fun getVehiclesByUserId(userId: VehicleId ): List<VehicleDTO> {
//        val vehicleEntity = dbQuery {
//            VehicleEntity.find(VehiclesTable.userId eq userId).firstOrNull()
//                ?: throw EntityWithIdNotFound("Vehicles with User ", userId)
//        }
//        return vehicleEntity.toVehicleDTO()
//    }

    override suspend fun getVehiclesByUserId(userId: VehicleId): List<VehicleDTO> {
        val vehicleEntities = dbQuery {
            VehicleEntity.find(VehiclesTable.userId eq userId).toList()
        }

        if (vehicleEntities.isEmpty()) {
            throw EntityWithIdNotFound("Vehicles for User ", userId)
        }

        return vehicleEntities.map { it.toVehicleDTO() }
    }

    override suspend fun updateVehicle(vehicleId: VehicleId, vehicle: UpdateVehicleDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVehicle(vehicleId: VehicleId) {
        TODO("Not yet implemented")
    }
}
