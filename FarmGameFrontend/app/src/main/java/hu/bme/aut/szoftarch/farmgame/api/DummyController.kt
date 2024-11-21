package hu.bme.aut.szoftarch.farmgame.api

import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land

class DummyController : Controller("") {

    override fun getPlayer(): Player {
        return Player(0, mutableMapOf(), 0, 0)
    }

    override fun getPossibleBuildings(): List<String> {

        return listOf("building_cow_shed", "building_sheep_pen")
    }

    override fun getPossibleCrops(): List<String> {
        return listOf("crop_flowers", "crop_wheat", "crop_corn")
    }

    override fun interact(land: Land, interaction: String, params: List<String>): Boolean {
        land.interact(interaction, params)
        return true
    }

    override fun getDisplayNames(): Map<String, String> {
        return mapOf(
            Pair("crop_wheat", "Wheat"),
            Pair("crop_flowers", "Flowers"),
            Pair("building_cow_shed", "Cow Shed"),
            Pair("crop_corn", "Corn"),
            Pair("crop_null", "No crops"),
            Pair("empty", "Empty"),
            Pair("action_build", "Build"),
            Pair("action_build:building_cow_shed", "Cow Shed"),
            Pair("action_build:building_sheep_pen", "Sheep Pen"),
            Pair("action_plough", "Plough"),
        )
    }
}