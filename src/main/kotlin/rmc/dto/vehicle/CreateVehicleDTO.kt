package rmc.dto.vehicle

import kotlinx.serialization.Serializable
import rmc.db.entity.EngineType

@Serializable
data class CreateVehicleDTO (
    val brand: String,
    val model: String,
    val year: Int,
    val vehicleClass: String,
    val engineType: EngineType,
    val licensePlate: String,
    val imgLink: String,
    val vehicleLocation: String,
    val price: Float,
    val availability: String
)