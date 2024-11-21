package hu.bme.aut.szoftarch.farmgame.feature.game

import hu.bme.aut.szoftarch.farmgame.api.Controller
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land

class Session(
    val controller: Controller,
) {
    //These could be used to cache stuff or to check availability with player level
    var buildings: MutableSet<String> = mutableSetOf()
    var crops: MutableSet<String> = mutableSetOf()

    var farm: Farm? = null
    suspend fun initialize() {
        farm = controller.getFarm()
        crops = controller.getPossibleCrops().toMutableSet()
        buildings = controller.getPossibleBuildings().toMutableSet()
    }

    fun registerBuilding(building: String) {
        buildings.add(building)
    }

    fun getPossibleBuildings(): List<String> {
        return buildings.toList()
    }

    fun registerCrop(crop: String) {
        crops.add(crop)
    }

    fun getPossibleCrops(): List<String> {
        return crops.toList()
    }

    fun getInteractions(land: Land): List<String> {
        if (land.content != null) {
            return land.content!!.getInteractions()
        }
        val possibleBuildings = getPossibleBuildings()
        val buildingActions = possibleBuildings.map {
            "action_build:$it"
        }
        return listOf( "action_plough").plus(buildingActions)
    }
}