package rmc.dto.vehicle

import kotlinx.serialization.Serializable
import rmc.db.dao.EngineType
import rmc.plugins.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class UpdateVehicleDTO(
    val brand: String,
    val model: String,
    val year: Int,
    val vehicleClass: String,
    val engineType: EngineType,
    val licensePlate: String,
    val imgLink: String,
    @Serializable(with = BigDecimalSerializer::class)
    val latitude: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val longitude: BigDecimal,
    val price: Double,
    val availability: Boolean
)