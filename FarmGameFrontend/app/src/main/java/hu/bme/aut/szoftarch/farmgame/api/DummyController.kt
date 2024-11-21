package hu.bme.aut.szoftarch.farmgame.api

import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.feature.quests.Quest

class DummyController(token: String) : Controller(token) {

    override fun getPlayer(): Player {
        return Player(0, mutableMapOf(), 0, 0)
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

    private var quests =mutableListOf(
    Quest(5, "Quest 1", "click a few times"),
    Quest(10, "Quest 2", "click some mmmmmore"),
    Quest(15, "Quest 3", "click some more"),
    Quest(20, "Quest 4", "click some more"),
    Quest(25, "Quest 5", "click some more"),
    Quest(30, "Quest 6", "click some more"),
    )

    override fun claimQuest(quest: Quest){
        quests.remove(quest)
    }
}