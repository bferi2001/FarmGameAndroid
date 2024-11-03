package hu.bme.aut.szoftarch.farmgame.data.farm

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

    override fun getInteractions(): List<String> {
        return listOf("crop_wheat", "crop_flowers")
    }

    override fun interact(interaction: String, params: List<String>) {
        content = Crop("Name", interaction)
    }


    fun Plant(crop: Crop){
        if (content != null){
            throw Exception("Planter is not empty")
        }
        content = crop
    }




}