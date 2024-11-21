package hu.bme.aut.szoftarch.farmgame.api

import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Building
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse

const val BACKEND_URL = "http://152.66.152.18:5153"
open class Controller(token: String) {
    var client = HttpClient(){
        defaultRequest {
            url(BACKEND_URL)
        }
    }

    private suspend fun get(location: String, token: String): HttpResponse{
        return client.get(location){
            header("Authorization", token)
        }
    }
    private suspend fun post(location: String, token: String): HttpResponse {
        return client.post(location){
            header("Authorization", token)
        }
    }

    open fun getPlayer(): Player {
        TODO("Not yet implemented")
    }

    open suspend fun getFarmSize(token: String): Int {
        val size = get("api/farm/size", token).body<Int>()
        return size
    }

    open fun getLands(): List<Land> {
        TODO("Not yet implemented")
    }

    suspend fun getFarm(token: String): Farm {
        val size = getFarmSize(token)
        val farm = Farm(size)
        for (land in getLands()) {
            farm.addLand(land)
        }
        return farm
    }

    open fun getPossibleBuildings(): List<String> {
        TODO("Not yet implemented")
    }

    open fun getBuildingActions(building: Building): List<String> {
        TODO("Not yet implemented")
    }

    open fun getPossibleCrops(): List<String> {
        TODO("Not yet implemented")
    }

    open fun getInteractions(land: Land): List<String> {
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

    open fun interact(land: Land, interaction: String, params: List<String>): Boolean {
        TODO("Not yet implemented")
    }

    open fun getDisplayNames(): Map<String, String> {
        TODO("Not yet implemented")
    }
}