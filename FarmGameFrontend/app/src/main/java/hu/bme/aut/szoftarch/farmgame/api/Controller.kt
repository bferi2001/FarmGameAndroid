package hu.bme.aut.szoftarch.farmgame.api

import androidx.compose.ui.graphics.Color
import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Building
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.feature.quests.Quest

open class Controller {
    //TODO get values from backend

    open fun getPlayer(id: Int): Player {
        TODO("Not yet implemented")
    }

    open fun getFarmSize(id: Int): Pair<Int, Int> {
        TODO("Not yet implemented")
    }

    open fun getLands(id: Int): List<Land> {
        TODO("Not yet implemented")
    }

    fun getFarm(id: Int): Farm {
        val size = getFarmSize(id)
        val farm = Farm(size.first, size.second)
        for (land in getLands(id)) {
            farm.addLand(land)
        }
        return farm
    }

    open fun getPossibleBuildings(id: Int): List<String> {
        TODO("Not yet implemented")
    }

    open fun getBuildingActions(building: Building): List<String> {
        TODO("Not yet implemented")
    }

    open fun getPossibleCrops(id: Int): List<String> {
        TODO("Not yet implemented")
    }

    open fun getInteractions(land: Land): List<String> {
        TODO("Not yet implemented")
    }

    open fun interact(land: Land, interaction: String, params: List<String>): Boolean {
        TODO("Not yet implemented")
    }

    open fun getDisplayNames(): Map<String, String> {
        TODO("Not yet implemented")
    }

    open fun getQuests(): List<Quest>{
        TODO("Not yet implemented")
    }

    open fun claimQuest(quest: Quest) {
        TODO("Not yet implemented")
    }
}