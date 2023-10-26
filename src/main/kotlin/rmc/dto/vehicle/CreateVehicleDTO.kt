package rmc.dto.vehicle

import kotlinx.serialization.Serializable
import rmc.db.dao.EngineType

@Serializable
data class CreateVehicleDTO (
    val brand: String,
    val model: String,
    val year: Int,
    val vehicleClass: String,
    val engineType: EngineType,
    val licensePlate: String,
    val imgLink: String,
    val latitude: Float,
    val longitude: Float,
    val price: Double,
    val availability: Boolean
)