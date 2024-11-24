package hu.bme.aut.szoftarch.farmgame.feature.game.farm

import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation

data class Land(
    val id: Int,
    val position: Int,
    var content: Buildable?
) {
    fun getInteractMenu(): MenuLocation {
        if (content != null) {
            return content!!.getInteractMenu()
        }
        return MenuLocation.BOTTOM
    }

    fun isProcessing(): Boolean {
        if (content != null) {
            return content!!.isProcessing()
        }
        return false
    }

    /**
     * Interact with the empty land
     *
     * @param interaction tag defining the type of interaction
     * @param params parameters of the interaction
     *
     * action_build:
     * param[0]: id of the building to build
     * param[1]: tag of building built
     *
     * action_crop:
     * param[0]: id of the planter
     */
    fun interact(interaction: String, params: List<String>):Boolean {
        if (content != null) {
            content!!.interact(interaction, params)
            return false
        }
        return true
    }

    fun getName(): String {
        return content?.getName() ?: "Empty Field"
    }

    fun getTag(): String {
        return content?.getTag() ?: "empty"
    }
}