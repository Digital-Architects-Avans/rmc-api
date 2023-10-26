package rmc.dto.rental

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import rmc.db.dao.RentalStatus

typealias RentalId = Int

@Serializable
data class RentalDTO(
    val id: Int,
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
