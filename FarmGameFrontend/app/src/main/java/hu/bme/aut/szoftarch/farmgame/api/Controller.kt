package hu.bme.aut.szoftarch.farmgame.api

import androidx.compose.ui.graphics.Color
import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Building
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

const val BACKEND_URL = "http://127.0.0.1:5153"
open class Controller {
    //TODO get values from backend
    val client = HttpClient(){
        defaultRequest {
            url(BACKEND_URL)
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens("token", "token")
                }
            }
        }
    }

    open fun getPlayer(id: Int): Player {
        TODO("Not yet implemented")
    }

    open suspend fun getFarmSize(id: Int): Int {
        val size = client.get("api/farm/size").body<Int>()
        return size
    }

    open fun getLands(id: Int): List<Land> {
        TODO("Not yet implemented")
    }

    suspend fun getFarm(id: Int): Farm {
        val size = getFarmSize(id)
        val farm = Farm(size)
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