package rmc.dao.rental

import org.jetbrains.exposed.dao.id.EntityID
import rmc.db.DatabaseFactory.dbQuery
import rmc.db.UsersTable
import rmc.db.VehiclesTable
import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.RentalDTO
import rmc.dto.vehicle.VehicleId
import rmc.error.EntityWithIdNotFound
import rmc.model.*

class RentalRepositoryImpl : RentalRepository {
    override suspend fun createRental(
        vehicleId: VehicleId,
        userId: Int,
        rental: CreateRentalDTO): RentalDTO = dbQuery {
            UserEntity.findById(userId) ?: throw EntityWithIdNotFound("User", userId)
            VehicleEntity.findById(vehicleId) ?: throw EntityWithIdNotFound("Vehicle", vehicleId)

        RentalEntity.new {
            this.vehicleId = EntityID(vehicleId, VehiclesTable)
            this.userId = EntityID(userId, UsersTable)
            date = rental.date
            price = rental.price
            location = rental.location
            status = RentalStatus.PENDING
            distanceTravelled = rental.distanceTravelled
            score = rental.score
        }.toRentalDTO()
    }
}