package rmc.dto.rental

import kotlinx.serialization.Serializable
import rmc.db.dao.RentalStatus

@Serializable
data class CreateRentalDTO(
    val date: String,
    val price: Float,
    val location: String,
    val status: RentalStatus,
    val distanceTravelled: Float,
    val score: Int
)
