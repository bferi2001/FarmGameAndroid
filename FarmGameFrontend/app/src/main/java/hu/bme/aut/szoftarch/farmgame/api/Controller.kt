package hu.bme.aut.szoftarch.farmgame.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hu.bme.aut.szoftarch.farmgame.api.dao.BarnDao
import hu.bme.aut.szoftarch.farmgame.api.dao.PlantedPlantDao
import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Building
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Planter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.gson.gson

const val BACKEND_URL = "http://152.66.152.18:5153/"

fun <T> Gson.fromJsonList(jsonString: String): List<T> =
    this.fromJson(jsonString, object: TypeToken<ArrayList<T>>() { }.type)

open class Controller(token: String) {
    var client = HttpClient(){
        defaultRequest {
            url(BACKEND_URL)
        }
        install(ContentNegotiation){
            gson()
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
        val res = get("api/farm/size", token)
        val size = res.body<Int>()
        return size
    }

    open suspend fun getLands(token: String, size: Int): List<Land> {
        var res = get("api/farm/Barn/barns", token)
        var json = res.bodyAsText()
        val gson = Gson()
        val barns = gson.fromJson(json, Array<BarnDao>::class.java)

        res = get("api/farm/plant/plantedPlants", token)
        json = res.bodyAsText()
        val plants = gson.fromJson(json, Array<PlantedPlantDao>::class.java)

        var lands = mutableListOf<Land>()

        for(barn in barns){
            val content = Building(
                id = barn.id,
                tag = barn.typeName
            )
            val land = Land(
                id = barn.id,
                position = barn.position,
                content = content
            )
            lands.add(land)
        }
        for(plant in plants){
            val content = Planter(
                id = plant.id,
            )
            val plantable = Land(
                id = plant.id,
                position = plant.position,
                content = content
            )
            lands.add(plantable)
        }

        lands.sortBy { it.position }

        while (lands.size < size){
            lands.add(Land(
                id = -1,
                position = lands.size,
                content = null
            ))
        }


        return lands
    }

    suspend fun getFarm(token: String): Farm {
        val size = getFarmSize(token)
        val farm = Farm(size)
        for (land in getLands(token, size)) {
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