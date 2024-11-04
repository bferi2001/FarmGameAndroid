package hu.bme.aut.szoftarch.farmgame.data.farm

import hu.bme.aut.szoftarch.farmgame.data.interaction.MenuLocation

class Planter(
    val id: Int,
) : Buildable() {
    var content: Crop? = null

    override fun GetName(): String {
        return content?.name ?: "Farm Land"
    }

    override fun GetTag(): String {
        return content?.tag ?: "crop_null"
    }

    override fun getInteractMenu(): MenuLocation {
        return MenuLocation.SIDE
    }

    override fun getInteractions(): List<String> {
        if (content == null) {
            return listOf("crop_wheat", "crop_flowers")
        }
        return listOf("wait")
    }

    override fun interact(interaction: String, params: List<String>) {
        if (interaction == "wait") return
        content = Crop("Planted Crop", interaction)
    }

    fun Plant(crop: Crop) {
        if (content != null) {
            throw Exception("Planter is not empty")
        }
        content = crop
    }
}