package rmc.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import rmc.db.dao.EngineType

object VehiclesTable : IntIdTable() {
    val userId = reference("userId", UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val brand = varchar("brand", 100)
    val model = varchar("model", 100)
    val year = integer("year").default(0)
    val vehicleClass =  varchar("vehicleClass", 100).default("")
    val engineType = enumerationByName<EngineType>("engineType", 100)
    val licensePlate = varchar("licensePlate", 100).default("")
    val imgLink = varchar("imgLink", 100).default("")
    val latitude = decimal("latitude", 8, 6)
    val longitude = decimal("longitude", 9, 6)
    val price = double("price").default(0.00)
    val availability = bool("availability").default(true)

    init {
        index(true, licensePlate)
    }
}