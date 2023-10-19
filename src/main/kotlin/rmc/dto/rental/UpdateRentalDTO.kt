package rmc.dto.rental

import kotlinx.serialization.Serializable
import rmc.db.dao.RentalStatus
import rmc.plugins.BigDecimalSerializer
import rmc.plugins.LocalDateSerializer
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class UpdateRentalDTO (
    val vehicleId: Int,
    val userId: Int,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val price: Float,
    @Serializable(with = BigDecimalSerializer::class)
    val latitude: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val longitude: BigDecimal,
    val status: RentalStatus,
    val distanceTravelled: Float,
    val score: Int
)