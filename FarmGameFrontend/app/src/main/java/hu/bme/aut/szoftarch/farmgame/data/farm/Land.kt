package hu.bme.aut.szoftarch.farmgame.data.farm

import hu.bme.aut.szoftarch.farmgame.data.building.CowShed
import hu.bme.aut.szoftarch.farmgame.data.building.SheepPen
import hu.bme.aut.szoftarch.farmgame.data.crop.Planter

data class Land(
    val id: Int,
    val position: Int,
    var content: String
    //var content: Buildable?
)
{
    fun GetBuildables(): List<Buildable>{
        return listOf(Planter(0), CowShed(0, 0), SheepPen(0, 0))
    }

    fun GetInteractions(){

    }

    fun Interact(interaction: String){

    }

    fun GetName(): String{
        return content
        //return content?.GetName() ?: "Empty Field"
    }

    fun GetTag(): String{
        return content
        //return content?.GetTag() ?: "empty"
    }

}