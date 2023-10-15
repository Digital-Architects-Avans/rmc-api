package rmc.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object VehiclesTable : IntIdTable() {
    val userId = reference("userId", UsersTable.id)
    val brand = varchar("brand", 100)
    val model = varchar("model", 100)
    val year = integer("year").default(0)
    val vehicleClass = varchar("vehicleClass", 100).default("")
    // TODO("engineType should be enumerationByName for different engineTypes (ICE, CEV, FBEV)")
    val engineType = varchar("engineType", 100).default("")
    val licensePlate = varchar("licensePlate", 100).default("")
    val imgLink = varchar("imgLink", 100).default("")
    val vehicleLocation = varchar("vehicleLocation", 100).default("")
    val price = float("price").default(0.0F)
    val availability = varchar("availability", 100).default("")

    init {
        index(true, licensePlate)
    }
}