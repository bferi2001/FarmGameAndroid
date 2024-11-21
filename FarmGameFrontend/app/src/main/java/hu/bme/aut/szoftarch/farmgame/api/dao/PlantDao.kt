package hu.bme.aut.szoftarch.farmgame.api.dao

import java.time.OffsetDateTime

data class PlantedPlantDao(
    var id: Int,
    var userName: String? = null,
    var cropsTypeName: String,
    var position: Int,
    var plantTime: OffsetDateTime,
    var harvestTime: OffsetDateTime? = null,
    var wateringTime: OffsetDateTime? = null,
    var weedingTime: OffsetDateTime? = null,
    var fertilisingTime: OffsetDateTime? = null
)