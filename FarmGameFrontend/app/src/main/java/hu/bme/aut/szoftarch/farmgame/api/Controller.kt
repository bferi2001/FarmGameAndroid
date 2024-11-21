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
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.gson.gson

const val BACKEND_URL = "http://192.168.68.71:5153/"

fun <T> Gson.fromJsonList(jsonString: String): List<T> =
    this.fromJson(jsonString, object: TypeToken<ArrayList<T>>() { }.type)

class Controller(val token: String) {
    val gson = Gson()
    var client = HttpClient(){
        defaultRequest {
            url(BACKEND_URL)
        }
        install(ContentNegotiation){
            gson()
        }
    }

    private suspend fun get(location: String): HttpResponse{
        return client.get(location){
            header("Authorization", token)
        }
    }
    private suspend fun post(location: String): HttpResponse {
        return client.post(location){
            header("Authorization", token)
        }
    }
    private suspend fun put(location: String): HttpResponse {
        return client.put(location){
            header("Authorization", token)
        }
    }

    open suspend fun getFarmSize(): Int {
        val res = get("api/farm/size")
        val size = res.body<Int>()
        return size
    }

    fun getDisplayNames(): Map<String, String> {
        return mapOf(
            Pair("crop_wheat", "Wheat"),
            Pair("crop_flowers", "Flowers"),
            Pair("building_cow", "Cow Shed"),
            Pair("crop_corn", "Corn"),
            Pair("crop_null", "No crops"),
            Pair("empty", "Empty"),
            Pair("action_build", "Build"),
            Pair("action_build:building_cow", "Cow Shed"),
            Pair("action_build:building_chicken", "Chicken Shed"),
            Pair("action_build:building_pig", "Pig Shed"),
            Pair("action_plough", "Plough"),
        )
    }

    open suspend fun getLands(size: Int): List<Land> {
        var res = get("api/farm/Barn/barns")
        var json = res.bodyAsText()
        val barns = gson.fromJson(json, Array<BarnDao>::class.java)

        res = get("api/farm/plant/plantedPlants")
        json = res.bodyAsText()
        val plants = gson.fromJson(json, Array<PlantedPlantDao>::class.java)

        var lands = mutableListOf<Land>()
        val displayNames = getDisplayNames()

        for(barn in barns){
            val content = Building(
                id = barn.id,
                tag = barn.typeName
            )
            content.level = barn.level
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
            val crop = Crop(
                name = displayNames[plant.cropsTypeName].toString(),
                tag = plant.cropsTypeName
            )
            content.content = crop

            val plantable = Land(
                id = plant.id,
                position = plant.position,
                content = content
            )
            lands.add(plantable)
        }

        var i = 0
        while (lands.size < size){
            if(!hasLandOnPosition(i, lands)){
                lands.add(Land(
                    id = -1,
                    position = i,
                    content = null
                ))
            }
            i++
        }
        lands.sortBy { it.position }
        return lands
    }
    private fun hasLandOnPosition(position: Int, lands: List<Land>): Boolean {
        for (land in lands) {
            if (land.position == position) {
                return true
            }
        }
        return false
    }

    suspend fun getFarm(): Farm {
        val size = getFarmSize()
        val farm = Farm(size)
        val lands = getLands(size)
        for (land in lands) {
            farm.addLand(land)
        }
        return farm
    }

    open suspend fun getPossibleBuildings(): List<String> {
        val res = get("api/farm/barn/unlocked")
        val buildings = res.body<Array<String>>()
        return buildings.toList()
    }

    open fun getBuildingActions(building: Building): List<String> {
        TODO("Not yet implemented")
    }

    open suspend fun getPossibleCrops(): List<String> {
        val res = get("api/farm/plant/unlocked")
        val crops = res.body<Array<String>>()
        return crops.toList()
    }

    private suspend fun upgradeBuilding(land: Land): Boolean {
        val position = land.position
        val res = put("api/farm/barn/$position/upgrade")
        return res.status.value == 200
    }
    private suspend fun buildBuilding(land: Land, building: String): Boolean {
        val position = land.position
        val res = post("api/farm/barn/$position/$building")
        return res.status.value == 200
    }

    open suspend fun interact(land: Land, interaction: String, params: List<String>): Boolean {
        if(land.content is Building)
        {
            val res = when(interaction){
                "upgrade" -> upgradeBuilding(land)
                "action_build" -> buildBuilding(land, params[0])
                else -> false
            }
            if(!res){
                return false
            }
        }
        else if(land.content is Planter)
        {
            val position = land.position
            val res = post("api/farm/plant/$position/$interaction")
            if(res.status.value != 200){
                return false
            }
        }
        else if(interaction == "action_build")
        {
            val res = buildBuilding(land, params[1])
            if(!res){
                return false
            }
        }

        return land.interact(interaction, params)
    }

    open suspend fun getQuests(): List<Quest>{
        var res = get("api/farm/quest/availableQuests")
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