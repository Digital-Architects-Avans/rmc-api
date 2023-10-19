package rmc.dto.rental

import kotlinx.serialization.Serializable
import rmc.plugins.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class CreateRentalDTO(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate
)
