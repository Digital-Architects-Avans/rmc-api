package rmc.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import rmc.db.dao.RentalStatus

object RentalsTable : IntIdTable() {
    val vehicleId = integer("vehicleId").references(VehiclesTable.id, onDelete = ReferenceOption.CASCADE)
    val userId = integer("userId").references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val date = date("date")
    val price = double("price").default(0.00)
    val latitude = float("latitude").default(0.0F)
    val longitude = float("longitude").default(0.0F)
    val status = enumerationByName<RentalStatus>("status", 100).default(RentalStatus.PENDING)
    val distanceTravelled = double("distanceTravelled").default(0.00)
    val score = integer("score").default(100)
}