package rmc.repository.rental

import org.jetbrains.exposed.dao.id.EntityID
import rmc.db.DatabaseFactory.dbQuery
import rmc.db.dao.*
import rmc.db.tables.UsersTable
import rmc.db.tables.VehiclesTable
import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.RentalDTO
import rmc.dto.rental.RentalId
import rmc.dto.rental.UpdateRentalDTO
import rmc.dto.user.UserId
import rmc.dto.vehicle.VehicleId
import rmc.error.EntityWithIdNotFound

class RentalRepositoryImpl : RentalRepository {

    override suspend fun allRentals(): List<RentalDTO> = dbQuery {
        RentalEntity.all().map {
            it.toRentalDTO()
        }
    }

    override suspend fun getRentalById(rentalId: RentalId): RentalDTO = dbQuery {
        RentalEntity.findById(rentalId)?.toRentalDTO() ?: throw EntityWithIdNotFound("Rental", rentalId)
    }

    override suspend fun createRental(
        userId: UserId,
        vehicleId: VehicleId,
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

    override suspend fun updateRental(rentalId: RentalId, rental: UpdateRentalDTO) = dbQuery {
        RentalEntity.findById(rentalId)?.let {
            it.vehicleId = EntityID(rental.vehicleId, VehiclesTable)
            it.userId = EntityID(rental.userId, UsersTable)
            it.date = rental.date
            it.price = rental.price
            it.location = rental.location
            it.status = rental.status
            it.distanceTravelled = rental.distanceTravelled
            it.score = rental.score
        } ?: throw EntityWithIdNotFound("Rental", rentalId)
    }

    override suspend fun deleteRental(rentalId: RentalId) = dbQuery {
        UserEntity.findById(rentalId)?.delete() ?: throw EntityWithIdNotFound("Rental", rentalId)
    }
}