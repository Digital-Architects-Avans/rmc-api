package rmc.dto.rental

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateRentalDTO(
    val date: LocalDate
)
