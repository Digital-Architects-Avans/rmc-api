package rmc.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import rmc.db.entity.EngineType

object VehiclesTable : IntIdTable() {
    val userId = reference("userId", UsersTable.id)
    val brand = varchar("brand", 100)
    val model = varchar("model", 100)
    val year = integer("year").default(0)
    val vehicleClass =  varchar("vehicleClass", 100).default("")
    val engineType = enumerationByName<EngineType>("EngineType", 100)
    val licensePlate = varchar("licensePlate", 100).default("")
    val imgLink = varchar("imgLink", 100).default("")
    val vehicleLocation = varchar("vehicleLocation", 100).default("")
    val price = float("price").default(0.0F)
    val availability = varchar("availability", 100).default("")

    init {
        index(true, licensePlate)
    }
}