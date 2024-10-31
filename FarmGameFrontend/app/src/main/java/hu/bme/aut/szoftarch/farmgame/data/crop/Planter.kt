package hu.bme.aut.szoftarch.farmgame.data.crop

import hu.bme.aut.szoftarch.farmgame.data.farm.Buildable

class Planter(
    val id: Int,
): Buildable(){
    var content: Crop? = null

    override fun GetName(): String {
        return content?.name ?: "Farm Land"
    }

    override fun GetTag(): String {
        return content?.tag ?: "crop_null"
    }

    fun Plant(crop: Crop){
        if (content != null){
            throw Exception("Planter is not empty")
        }
        content = crop
    }




}