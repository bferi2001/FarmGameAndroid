package hu.bme.aut.szoftarch.farmgame.api

import androidx.compose.ui.graphics.Color
import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Building
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Crop
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Planter

class DummyController : Controller() {

    override fun getPlayer(id: Int): Player {
        return Player(id, mutableMapOf(), 0, 0)
    }

    override fun getFarmSize(id: Int): Pair<Int, Int> {
        return Pair(10, 10)
    }

    override fun getLands(id: Int): List<Land> {
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

    override fun getPossibleBuildings(id: Int): List<String> {

        return listOf("building_cow_shed")
    }

    override fun getPossibleCrops(id: Int): List<String> {
        return listOf("crop_flowers", "crop_wheat", "crop_corn")
    }

    override fun getInteractions(land: Land): List<String> {
        /*  Decide on backend
            Example for new behaviour:
            -Check whether an extra action is available or not
            -Check whether player unlocked the land or not
        */
        if (land.content != null) {
            return land.content!!.getInteractions()
        }
        return listOf("action_build", "action_plough")
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
        )
    }

    override fun getImages(): MutableMap<String, Color> {
        return mutableMapOf(
            Pair("crop_wheat", Color(0xFFF8B423)),
            Pair("crop_flowers", Color(0xFF7F00FF)),
            Pair("building_cow_shed", Color.Gray),
            Pair("building_cow_shed_dirty", Color.DarkGray),
            Pair("crop_corn", Color(0xFFF8B423)),
            Pair("crop_null", Color(0xFF964B00)),
            Pair("empty", Color(0xFF3f9b0b)),
        )
    }


}