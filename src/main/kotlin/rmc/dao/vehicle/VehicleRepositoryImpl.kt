package rmc.dao.vehicle

import org.jetbrains.exposed.dao.id.EntityID
import rmc.db.DatabaseFactory.dbQuery
import rmc.db.UsersTable
import rmc.db.VehiclesTable
import rmc.dto.user.UserId
import rmc.dto.vehicle.CreateVehicleDTO
import rmc.dto.vehicle.VehicleDTO
import rmc.error.EntityWithIdNotFound
import rmc.error.VehicleAlreadyRegistered
import rmc.model.UserEntity
import rmc.model.VehicleEntity
import rmc.model.toVehicleDTO

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
}
