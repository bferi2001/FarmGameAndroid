package hu.bme.aut.szoftarch.farmgame.api.dao

data class PlantedPlantDao(
    var id: Int,
    var userName: String? = null,
    var cropsTypeName: String,
    var position: Int,
    var plantTime: String,
    var harvestTime: String? = null,
    var wateringTime: String? = null,
    var weedingTime: String? = null,
    var fertilisingTime: String? = null
)