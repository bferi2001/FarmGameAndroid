package hu.bme.aut.szoftarch.farmgame.data.farm

abstract class Buildable {
    abstract fun GetName(): String
    abstract fun GetTag():String

    abstract fun getInteractions(): List<String>
    abstract fun interact(interaction: String, params: List<String>)
}