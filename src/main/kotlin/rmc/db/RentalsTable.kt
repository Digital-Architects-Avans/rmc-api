package rmc.db

import org.jetbrains.exposed.dao.id.IntIdTable
import rmc.db.entity.RentalStatus

object RentalsTable : IntIdTable() {
    val vehicleId = reference("vehicleId", VehiclesTable.id)
    val userId = reference("userId", UsersTable.id)
    val date = varchar("date", 100)
    val price = float("price").default(0.0F)
    val location = varchar("location", 100).default("")
    val status = enumerationByName<RentalStatus>("status", 100).default(RentalStatus.PENDING)
    val distanceTravelled = float("distanceTravelled").default(0.0F)
    val score = integer("score").default(100)
}