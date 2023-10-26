package rmc.dto.rental

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import rmc.db.dao.RentalStatus

@Serializable
data class UpdateRentalDTO (
    val vehicleId: Int,
    val userId: Int,
    val date: LocalDate,
    val price: Double,
    val latitude: Float,
    val longitude: Float,
    val status: RentalStatus,
    val distanceTravelled: Double,
    val score: Int
)