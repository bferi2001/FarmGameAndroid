package hu.bme.aut.szoftarch.farmgame.feature.game.farm

import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation
import java.util.Date

abstract class Buildable {
    abstract fun getName(): String
    abstract fun getTag(): String
    abstract fun setNewActions(actions: Array<String>?)

    abstract fun getInteractMenu(): MenuLocation
    abstract fun getInteractions(): List<String>
    abstract fun isProcessing(): Boolean
    abstract fun getTargetDate(): Date
    abstract fun getStartDate(): Date
    abstract fun interact(interaction: String, params: List<String>)
}