package hu.bme.aut.szoftarch.farmgame.data.building

import hu.bme.aut.szoftarch.farmgame.data.farm.Buildable

abstract class Building (
    val id: Int,
    var level: Int,
):Buildable(){
    abstract val name: String
    abstract val tag: String

    override fun GetName(): String {
        return name
    }

    override fun GetTag(): String {
        return tag
    }
}