package rmc.db.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import rmc.db.VehiclesTable
import rmc.dto.vehicle.VehicleDTO

class VehicleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VehicleEntity>(VehiclesTable)

    var brand by VehiclesTable.brand
    var model by VehiclesTable.model
    var year by VehiclesTable.year
    var vehicleClass by VehiclesTable.vehicleClass
    var engineType by VehiclesTable.engineType
    var licensePlate by VehiclesTable.licensePlate
    var imgLink by VehiclesTable.imgLink
    var vehicleLocation by VehiclesTable.vehicleLocation
    var price by VehiclesTable.price
    var availability by VehiclesTable.availability

    // Simple one to one reference from the UsersTable
    var userId by VehiclesTable.userId
}

fun VehicleEntity.toVehicleDTO() = VehicleDTO(
    this.id.value,
    this.userId.value,
    this.brand,
    this.model,
    this.year,
    this.vehicleClass,
    this.engineType,
    this.licensePlate,
    this.imgLink,
    this.vehicleLocation,
    this.price,
    this.availability
)