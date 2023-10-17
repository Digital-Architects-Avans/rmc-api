package rmc.dto.vehicle

import kotlinx.serialization.Serializable

@Serializable
data class UpdateVehicleDTO(
    val userId: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val vehicleClass: String,
    // TODO("engineType should be enumerationByName for different engineTypes (ICE, CEV, FBEV)")
    val engineType: String,
    val licensePlate: String,
    val imgLink: String,
    val vehicleLocation: String,
    val price: Float,
    val availability: String
)