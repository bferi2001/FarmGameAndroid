package hu.bme.aut.szoftarch.farmgame.api

import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Building
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Crop
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Planter

class DummyController : Controller("") {

    override fun getPlayer(): Player {
        return Player(0, mutableMapOf(), 0, 0)
    }

    override fun getLands(): List<Land> {
        //Dummy starter lands
        val flowers = Planter(66)
        flowers.content = Crop("flowers", "crop_flowers")
        val wheat = Planter(67)
        wheat.content = Crop("wheat", "crop_wheat")
        return listOf(
            Land(134, 34, flowers),
            Land(135, 35, wheat),
            Land(136, 75, Building(55, "building_cow_shed")),
            Land(137, 85, Building(57, "building_cow_shed")),
        )
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