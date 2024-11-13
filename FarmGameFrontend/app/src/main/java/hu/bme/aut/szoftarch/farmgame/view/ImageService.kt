package hu.bme.aut.szoftarch.farmgame.view

import hu.bme.aut.szoftarch.farmgame.R

object ImageService {
    var images = mutableMapOf<String, Int>()

    init {
        images =  mutableMapOf(
            Pair("crop_wheat", R.drawable.wheat_grown_tile),
            Pair("crop_null", R.drawable.planter_tile),
            Pair("empty", R.drawable.empty_tile),
        )
    }

    fun getImage(tag: String): Int {
        return images.getOrDefault(tag, R.drawable.missing_tile)
    }
}