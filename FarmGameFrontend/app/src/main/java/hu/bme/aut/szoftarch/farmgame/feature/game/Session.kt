package hu.bme.aut.szoftarch.farmgame.feature.game

import hu.bme.aut.szoftarch.farmgame.api.Controller
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land

class Session(
    val controller: Controller,
) {
    //These could be used to cache stuff or to check availability with player level
    val buildings: MutableSet<String> = controller.getPossibleBuildings().toMutableSet()
    val crops: MutableSet<String> = controller.getPossibleCrops().toMutableSet()

    var farm: Farm? = null
    suspend fun initialize(token: String) {
       farm = controller.getFarm(token)
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
        return controller.getInteractions(land)
    }
}