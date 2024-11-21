package hu.bme.aut.szoftarch.farmgame.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hu.bme.aut.szoftarch.farmgame.api.dao.BarnDao
import hu.bme.aut.szoftarch.farmgame.api.dao.PlantedPlantDao
import hu.bme.aut.szoftarch.farmgame.api.dao.QuestTypeDao
import hu.bme.aut.szoftarch.farmgame.feature.game.Player
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Buildable
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Building
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Crop
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Planter
import hu.bme.aut.szoftarch.farmgame.feature.quests.Quest
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

const val BACKEND_URL = "http://192.168.68.71:5153/"

fun <T> Gson.fromJsonList(jsonString: String): List<T> =
    this.fromJson(jsonString, object: TypeToken<ArrayList<T>>() { }.type)

open class Controller(val token: String) {
    val gson = Gson()
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

    open suspend fun getFarmSize(): Int {
        val res = get("api/farm/size", token)
        val size = res.body<Int>()
        return size
    }

    open suspend fun getLands(size: Int): List<Land> {
        var res = get("api/farm/Barn/barns", token)
        var json = res.bodyAsText()
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

    suspend fun getFarm(): Farm {
        val size = getFarmSize()
        val farm = Farm(size)
        for (land in getLands(size)) {
            farm.addLand(land)
        }
        return farm
    }

    open suspend fun getPossibleBuildings(): List<String> {
        val res = get("api/farm/barn/unlocked", token)
        val buildings = res.body<Array<String>>()
        return buildings.toList()
    }

    open fun getBuildingActions(building: Building): List<String> {
        TODO("Not yet implemented")
    }

    open suspend fun getPossibleCrops(): List<String> {
        val res = get("api/farm/plant/unlocked", token)
        val crops = res.body<Array<String>>()
        return crops.toList()
    }

    open fun getInteractions(land: Land): List<String> {
        TODO("Not yet implemented")
    }

    open suspend fun interact(land: Land, interaction: String, params: List<String>): Boolean {
        val position = land.position
        if(land.content is Building)
        {
            val res = post("api/farm/plant/$position/$interaction", token)
            return res.status.value == 200
        }
        else if(land.content is Planter)
        {
            val res = post("api/farm/barn/$position/$interaction", token)
            return res.status.value == 200
        }
        else
        {
            return land.interact(interaction, params)
        }
    }

    open fun getDisplayNames(): Map<String, String> {
        TODO("Not yet implemented")
    }

    open suspend fun getQuests(): List<Quest>{
        var res = get("api/farm/quest/availableQuests", token)
        var json = res.bodyAsText()
        val quests = gson.fromJson(json, Array<QuestTypeDao>::class.java)

        var questList = mutableListOf<Quest>()
        for(quest in quests){
            questList.add(Quest(goal = 0, description = quest.description, title = "Quest-${quest.id}"))
        }
        return questList
    }

    open fun claimQuest(quest: Quest) {
        TODO("Not yet implemented")
    }
}