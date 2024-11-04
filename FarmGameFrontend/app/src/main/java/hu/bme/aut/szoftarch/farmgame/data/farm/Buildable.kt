package hu.bme.aut.szoftarch.farmgame.data.farm

import hu.bme.aut.szoftarch.farmgame.data.interaction.MenuLocation

abstract class Buildable {
    abstract fun GetName(): String
    abstract fun GetTag(): String

    abstract fun getInteractMenu(): MenuLocation
    abstract fun getInteractions(): List<String>
    abstract fun interact(interaction: String, params: List<String>)
}