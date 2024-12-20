package hu.bme.aut.szoftarch.farmgame.data.game

import androidx.compose.runtime.remember
import hu.bme.aut.szoftarch.farmgame.data.farm.Farm

class Session(columns: Int, rows: Int) {
    val buildings: MutableSet<String> = mutableSetOf()
    val crops: MutableSet<String> = mutableSetOf()
    val farm = Farm(columns,rows)

    fun RegisterBuilding(building: String) {
        buildings.add(building)
    }

    fun GetBuildings(): List<String> {
        return buildings.toList()
    }

    fun RegisterCrop(crop: String) {
        crops.add(crop)
    }

    fun GetCrops(): List<String> {
        return crops.toList()
    }



}