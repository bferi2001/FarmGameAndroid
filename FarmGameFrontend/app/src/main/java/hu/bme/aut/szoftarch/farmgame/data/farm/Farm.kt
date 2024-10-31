package hu.bme.aut.szoftarch.farmgame.data.farm

class Farm (
    val columns: Int,
    val rows: Int,
){
    val lands: MutableList<Land>

    init{
        val length = columns*rows
        lands = MutableList(length){ Land(it, it,"empty") }
    }

    fun GetLand(position: Int): Land {
        return lands.get(position)
    }

    fun AddLand(land: Land){
        if (land.position >= lands.size){
            throw Exception("Land position out of range")
        }
        lands.set(land.position, land)
    }

}