package rmc.dto.vehicle

import kotlinx.serialization.Serializable

@Serializable
data class CreateVehicleDTO (
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