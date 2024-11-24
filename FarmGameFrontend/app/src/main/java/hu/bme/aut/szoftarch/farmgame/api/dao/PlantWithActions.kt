package hu.bme.aut.szoftarch.farmgame.api.dao

data class PlantWithActions(
    val plant: PlantedPlantDao,
    val actions: Array<String>
)
