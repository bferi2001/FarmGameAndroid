package hu.bme.aut.szoftarch.farmgame.api

import androidx.compose.ui.graphics.Color
import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Building
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Crop
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Planter
import hu.bme.aut.szoftarch.farmgame.feature.quests.Quest

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

        return listOf("building_cow_shed", "building_sheep_pen")
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
        return listOf( "action_plough","action_build:building_cow_shed", "action_build:building_sheep_pen",)
    }

    override fun interact(land: Land, interaction: String, params: List<String>): Boolean {
        return land.interact(interaction, params)
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

    private var quests =mutableListOf(
    Quest(5, "Quest 1", "click a few times"),
    Quest(10, "Quest 2", "click some mmmmmore"),
    Quest(15, "Quest 3", "click some more"),
    Quest(20, "Quest 4", "click some more"),
    Quest(25, "Quest 5", "click some more"),
    Quest(30, "Quest 6", "click some more"),
    )

    override fun getQuests(): List<Quest> {
        return quests
    }

    override fun claimQuest(quest: Quest){
        quests.remove(quest)
    }
}