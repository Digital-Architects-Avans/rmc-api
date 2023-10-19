package rmc.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import rmc.db.dao.RentalStatus

object RentalsTable : IntIdTable() {
    val vehicleId = reference("vehicleId", VehiclesTable.id)
    val userId = reference("userId", UsersTable.id)
    val date = date("date")
    val price = float("price").default(0.0F)
    val latitude = decimal("latitude", 8, 6)
    val longitude = decimal("longitude", 9, 6)
    val status = enumerationByName<RentalStatus>("status", 100).default(RentalStatus.PENDING)
    val distanceTravelled = float("distanceTravelled").default(0.0F)
    val score = integer("score").default(100)
}