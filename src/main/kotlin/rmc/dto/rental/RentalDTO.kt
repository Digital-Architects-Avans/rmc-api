package rmc.dto.rental

import kotlinx.serialization.Serializable
import rmc.db.dao.RentalStatus
import rmc.plugins.BigDecimalSerializer
import rmc.plugins.LocalDateSerializer
import java.math.BigDecimal
import java.time.LocalDate

typealias RentalId = Int

@Serializable
data class RentalDTO(
    val id: Int,
    val vehicleId: Int,
    val userId: Int,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val price: Double,
    @Serializable(with = BigDecimalSerializer::class)
    val latitude: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val longitude: BigDecimal,
    val status: RentalStatus,
    val distanceTravelled: Double,
    val score: Int
)
