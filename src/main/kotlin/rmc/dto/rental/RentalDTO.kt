package rmc.dto.rental

import kotlinx.serialization.Serializable

@Serializable
data class RentalDTO(
    val id: Int,
    val vehicleId: Int,
    val userId: Int,
    val date: String,
    val price: Float,
    val location: String,
    val status: String,
    val distanceTravelled: Float,
    val score: Int
)
