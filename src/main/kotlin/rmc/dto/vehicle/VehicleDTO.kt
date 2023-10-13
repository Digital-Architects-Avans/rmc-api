package rmc.dto.vehicle

import kotlinx.serialization.Serializable

typealias VehicleId = Int
@Serializable
data class VehicleDTO (
    val id: Int,
    val userId: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val vehicleClass: String,
    val engineType: String,
    val licensePlate: String,
    val imgLink: String,
    val vehicleLocation: String,
    val price: Float,
    val availability: String
)