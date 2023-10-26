package rmc.dto.vehicle

import kotlinx.serialization.Serializable
import rmc.db.dao.EngineType

typealias VehicleId = Int

@Serializable
data class VehicleDTO (
    val id: Int,
    val userId: Int,
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