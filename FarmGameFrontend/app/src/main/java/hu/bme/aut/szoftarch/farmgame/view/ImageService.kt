package hu.bme.aut.szoftarch.farmgame.view

import hu.bme.aut.szoftarch.farmgame.R

object ImageService {
    var images = mutableMapOf<String, Int>()

    init {
        images =  mutableMapOf(
            Pair("crop_wheat", R.drawable.wheat_grown_tile),
            Pair("crop_wheat:growing", R.drawable.wheat_growing_tile),
            Pair("crop_flower", R.drawable.flower_grown_tile),
            Pair("crop_flower:growing", R.drawable.flower_growing_tile),
            Pair("crop_carrot", R.drawable.carrot_grown_tile),
            Pair("crop_carrot:growing", R.drawable.carrot_growing_tile),
            Pair("crop_null", R.drawable.planter_tile),
            Pair("building_cow", R.drawable.building_cow_tile_lvl1),
            Pair("building_cow:medium", R.drawable.building_cow_tile_lvl2),
            Pair("building_cow:big", R.drawable.building_cow_tile_lvl3),
            Pair("building_chicken", R.drawable.building_chicken_tile_lvl1),
            Pair("building_chicken:medium", R.drawable.building_chicken_tile_lvl2),
            Pair("building_chicken:big", R.drawable.building_chicken_tile_lvl3),
            Pair("empty", R.drawable.empty_tile),
        )
    }

    fun getImage(tag: String): Int {
        return images.getOrDefault(tag, R.drawable.missing_tile)
    }
}