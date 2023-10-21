package rmc.repository.rental

import rmc.db.DatabaseFactory.dbQuery
import rmc.db.dao.*
import rmc.db.tables.RentalsTable
import rmc.dto.rental.CreateRentalDTO
import rmc.dto.rental.RentalDTO
import rmc.dto.rental.RentalId
import rmc.dto.rental.UpdateRentalDTO
import rmc.dto.user.UserId
import rmc.dto.vehicle.VehicleId
import rmc.error.EntityWithIdNotFound
import rmc.error.NoRentalsForUserFound
import rmc.repository.vehicle.vehicleRepository
import java.time.LocalDate

class RentalRepositoryImpl : RentalRepository {

    override suspend fun allRentals(): List<RentalDTO> = dbQuery {
        RentalEntity.all().map {
            it.toRentalDTO()
        }
    }

    override suspend fun getRentalById(rentalId: RentalId): RentalDTO = dbQuery {
        RentalEntity.findById(rentalId)?.toRentalDTO() ?: throw EntityWithIdNotFound("Rental", rentalId)
    }

    override suspend fun getRentalByUserId(userId: UserId): List<RentalDTO> = dbQuery {
        RentalEntity.find { RentalsTable.userId eq userId }
            .map { it.toRentalDTO() }
            .takeIf { it.isNotEmpty() } ?: throw NoRentalsForUserFound(userId)
    }

    override suspend fun getRentalByVehicleId(vehicleId: VehicleId): List<RentalDTO> = dbQuery {
        RentalEntity.find { RentalsTable.vehicleId eq vehicleId }.map { it.toRentalDTO() }
    }

    override suspend fun createRental(
        userId: UserId,
        vehicleId: VehicleId,
        rental: CreateRentalDTO
    ): RentalDTO = dbQuery {
        UserEntity.findById(userId) ?: throw EntityWithIdNotFound("User", userId)
        VehicleEntity.findById(vehicleId) ?: throw EntityWithIdNotFound("Vehicle", vehicleId)
        val vehicle = vehicleRepository.getVehicleById(vehicleId)

        RentalEntity.new {
            this.vehicleId = vehicleId
            this.userId = userId
            date = rental.date
            price = vehicle.price
            longitude = vehicle.longitude
            latitude = vehicle.latitude
            status = RentalStatus.PENDING
        }.toRentalDTO()
    }

    override suspend fun updateRental(rentalId: RentalId, rental: UpdateRentalDTO) = dbQuery {
        RentalEntity.findById(rentalId)?.let {
            it.vehicleId = rental.vehicleId
            it.userId = rental.userId
            it.date = rental.date
            it.price = rental.price
            it.longitude = rental.longitude
            it.latitude = rental.latitude
            it.status = rental.status
            it.distanceTravelled = rental.distanceTravelled
            it.score = rental.score
        } ?: throw EntityWithIdNotFound("Rental", rentalId)
    }

    override suspend fun updateRentalStatus(rentalId: RentalId, status: RentalStatus) = dbQuery {
        RentalEntity.findById(rentalId)?.let {
            it.status = status
        } ?: throw EntityWithIdNotFound("Rental", rentalId)
    }

    // For every rental for a vehicle on the same date, if not approvedRental set status to denied
    override suspend fun cascadeRentalStatus(approvedRental: RentalId, vehicleId: VehicleId, date: LocalDate) = dbQuery {
        for (rentalEntity in RentalEntity.find { RentalsTable.vehicleId eq vehicleId }.filter { it.date == date }) {
            if (rentalEntity.id.value != approvedRental) this.updateRentalStatus(rentalEntity.id.value, RentalStatus.DENIED)
        }
    }

    override suspend fun deleteRental(rentalId: RentalId) = dbQuery {
        RentalEntity.findById(rentalId)?.delete() ?: throw EntityWithIdNotFound("Rental", rentalId)
    }
}
val rentalRepository: RentalRepository = RentalRepositoryImpl()
