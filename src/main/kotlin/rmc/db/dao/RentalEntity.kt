package rmc.db.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import rmc.db.tables.RentalsTable
import rmc.dto.rental.RentalDTO

enum class RentalStatus(val status: String) {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    DENIED("DENIED"),
    CANCELLED("CANCELLED")
}
class RentalEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RentalEntity>(RentalsTable)

    var date by RentalsTable.date
    var price by RentalsTable.price
    var location by RentalsTable.location
    var status by RentalsTable.status
    var distanceTravelled by RentalsTable.distanceTravelled
    var score by RentalsTable.score

    // Simple one to one reference from the VehiclesTable and UsersTable
    var userId by RentalsTable.userId
    var vehicleId by RentalsTable.vehicleId
}

fun RentalEntity.toRentalDTO() = RentalDTO(
    this.id.value,
    this.vehicleId.value,
    this.userId.value,
    this.date,
    this.price,
    this.location,
    this.status,
    this.distanceTravelled,
    this.score
)