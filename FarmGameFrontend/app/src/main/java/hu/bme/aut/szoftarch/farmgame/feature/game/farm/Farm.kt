package hu.bme.aut.szoftarch.farmgame.feature.game.farm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Farm(
    val columns: Int,
    val rows: Int,
) {
    var lands: List<Land> by
    mutableStateOf(emptyList())
        private set

    init {
        lands = List(columns * rows) { Land(it, it, null) }
    }

    fun getLand(position: Int): Land {
        return lands[position]
    }

    fun addLand(land: Land) {
        if (land.position >= lands.size) {
            throw Exception("Land position out of range")
        }
        lands = lands.toMutableList().also { it[land.position] = land }
    }

    fun refreshLand(land: Land) {
        addLand(land)
    }

}