package rmc.dto.rental

import kotlinx.serialization.Serializable
import rmc.db.dao.RentalStatus

@Serializable
data class UpdateRentalDTO (
    val vehicleId: Int,
    val userId: Int,
    val date: String,
    val price: Float,
    val location: String,
    val status: RentalStatus,
    val distanceTravelled: Float,
    val score: Int
)