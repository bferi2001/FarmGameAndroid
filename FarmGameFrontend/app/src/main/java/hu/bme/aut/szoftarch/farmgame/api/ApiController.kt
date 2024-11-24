package hu.bme.aut.szoftarch.farmgame.api

import hu.bme.aut.szoftarch.farmgame.api.dao.BarnWithActionsDao
import hu.bme.aut.szoftarch.farmgame.api.dao.ClassifiedDao
import hu.bme.aut.szoftarch.farmgame.api.dao.MarketUserProductDao
import hu.bme.aut.szoftarch.farmgame.api.dao.PlantWithActions
import hu.bme.aut.szoftarch.farmgame.api.dao.PlantedPlantDao
import hu.bme.aut.szoftarch.farmgame.api.dao.QuestDao
import hu.bme.aut.szoftarch.farmgame.api.dao.UserDao
import hu.bme.aut.szoftarch.farmgame.data.market.AdItemData
import hu.bme.aut.szoftarch.farmgame.data.market.SellingItemData
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Building
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Crop
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Planter
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.User
import hu.bme.aut.szoftarch.farmgame.feature.quests.Quest
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApiController(token: String) : HttpRequestMaker(token) {
    suspend fun getFarmSize(): Int {
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
            Pair("building_cow", "Cow Shed"),
            Pair("building_chicken", "Chicken Shed"),
            Pair("building_pig", "Pig Shed"),
            Pair("crop_wheat", "Wheat"),
            Pair("crop_carrot", "Carrot"),
            Pair("crop_potato", "Potato"),
            Pair("crop_flower", "Flower"),
        )
    }

    suspend fun getLands(size: Int): List<Land> {
        var res = get("api/farm/Barn/barns")
        var json = res.bodyAsText()
        val barns = gson.fromJson(json, Array<BarnWithActionsDao>::class.java)

        res = get("api/farm/plant/plantedPlants")
        json = res.bodyAsText()
        val plants = gson.fromJson(json, Array<PlantWithActions>::class.java)

        var lands = mutableListOf<Land>()
        val displayNames = getDisplayNames()

        for(barnWithActions in barns){
            val barn = barnWithActions.barn
            val content = Building(
                id = barn.id,
                tag = barn.typeName,
                actions = barnWithActions.actions,
                productionStartTime = toLocalDateTime(barn.productionStartTime),
                productionEndTime = toLocalDateTime(barn.productionEndTime)
            )
            content.level = barn.level
            val land = Land(
                id = barn.id,
                position = barn.position,
                content = content
            )
            lands.add(land)
        }
        for(plantWithActions in plants){
            val plant = plantWithActions.plant
            val content = Planter(
                id = plant.id,
                plantTime = toLocalDateTime(plant.plantTime),
                actions = plantWithActions.actions,
                harvestTime = if(plant.harvestTime == null) null else toLocalDateTime(plant.harvestTime!!),
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

    private fun toLocalDateTime(date: String): LocalDateTime {
        val modifiedDate = date.replace("T", " ").split('.')[0]
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.parse(modifiedDate, formatter)
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

    suspend fun getPossibleBuildings(): List<String> {
        val res = get("api/farm/barn/unlocked")
        val buildings = res.body<Array<String>>()
        return buildings.toList()
    }

    suspend fun getPossibleCrops(): List<String> {
        val res = get("api/farm/plant/unlocked")
        val crops = res.body<Array<String>>()
        return crops.toList()
    }
    private suspend fun buildBuilding(land: Land, building: String): Boolean {
        val position = land.position
        val res = post("api/farm/barn/$position/$building")
        return res.status.value == 200
    }
    private suspend fun buildPlanter(land: Land, plant: String): Boolean {
        val position = land.position
        val res = post("api/farm/plant/$position/$plant")
        return res.status.value == 200
    }

    suspend fun interact(land: Land, interaction: String, params: List<String>): Boolean {
        if(land.content is Building)
        {
            if(interaction == "action_build")
            {
                val res = buildBuilding(land, params[0])
                return res
            }
            else if(interaction == "cleaning"
                || interaction == "feeding"
                || interaction == "harvesting"
                || interaction == "upgrade")
            {
                return barnAction(land.position, interaction)
            }
        }
        else if(land.content is Planter)
        {
            if(interaction == "harvesting")
            {
                return harvest(land.position)
            }
            else if(
                interaction == "watering" ||
                interaction == "fertilising" ||
                interaction == "weeding" )
            {
                return plantAction(land.position, interaction)
            }
            else {
                val position = land.position
                val res = post("api/farm/plant/$position/$interaction")
                if(res.status.value != 200){
                    return false
                }
            }
        }
        else if(interaction == "action_build")
        {
            val res = buildBuilding(land, params[1])
            if(!res){
                return false
            }
        }
        else if(interaction == "action_crop")
        {
            val res = buildPlanter(land, params[1])
            if(!res){
                return false
            }
        }

        return land.interact(interaction, params)
    }

    suspend fun getQuests(): List<Quest>{
        var res = get("api/farm/quest/availableQuests")
        var json = res.bodyAsText()
        val quests = gson.fromJson(json, Array<QuestDao>::class.java)

        var questList = mutableListOf<Quest>()
        for(quest in quests){
            val description = quest.taskKeyword + " " + quest.objectId
            questList.add(
                Quest(
                    goal = quest.goalQuantity,
                    description = description,
                    title = "Quest-${quest.id}",
                    progress = quest.currentQuantity,
                    rewardMoney = quest.rewardMoney,
                    rewardXP = quest.rewardXP
                ))
        }
        return questList
    }

    suspend fun getAds(): List<AdItemData> {
        val res = get("api/farm/currentuser/market")
        val json = res.bodyAsText()
        val adsDao = gson.fromJson(json, Array<ClassifiedDao>::class.java)
        val ads = adsDao.map {
            AdItemData(
                item = it.productName,
                price = it.price,
                count = it.quantity,
                seller = it.userName ?: "Unknown",
                deadline = it.deadline
            )
        }

        return ads.toList()
    }

    suspend fun getInventory(): List<SellingItemData> {
        val res = get("api/farm/currentuser/inventory/market")
        val json = res.bodyAsText()
        val inventoryDao = gson.fromJson(json, Array<MarketUserProductDao>::class.java)
        val inventory = inventoryDao.map {
            SellingItemData(
                item = it.productName,
                price = it.quickSellPrice,
                quantity = it.quantity
            )
        }
        return inventory
    }

    suspend fun getActions(position: Int, plantActions: Boolean): Array<String> {
        val type = if(plantActions) "plant" else "barn"
        val res = get("api/farm/$type/$position/actions")
        val json = res.bodyAsText()
        val actions = gson.fromJson(json, Array<String>::class.java)
        if(actions == null){
            return arrayOf()
        }
        return actions
    }

    suspend fun harvest(position: Int): Boolean{
        val res = delete("api/farm/plant/$position/harvest")
        return res.status.value == 204
    }

    suspend fun plantAction(position: Int, action: String): Boolean {
        val res = put("api/farm/plant/$position/$action")
        return res.status.value == 204
    }
    suspend fun barnAction(position: Int, action: String): Boolean {
        val res = put("api/farm/barn/$position/$action")
        return res.status.value == 204
    }

    suspend fun getCurrentUser(): User {
        val res = get("api/currentUser")
        val json = res.bodyAsText()
        val userDao = gson.fromJson(json, UserDao::class.java)
        return User(
            userXP = userDao.userXP,
            userMoney = userDao.userMoney
        )
    }

    suspend fun quickSell(sellingItemData: SellingItemData): Boolean {
        val res = put("api/farm/market/quicksell/${sellingItemData.item}/${sellingItemData.quantity}")
        return res.status.value == 204
    }

    suspend fun createAd(sellingItemData: SellingItemData): Boolean {
        val res = post("api/farm/market/${sellingItemData.item}/${sellingItemData.quantity}/${sellingItemData.price}")
        return res.status.value == 204
    }
}