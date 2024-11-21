package hu.bme.aut.szoftarch.farmgame.feature.game.farm

import hu.bme.aut.szoftarch.farmgame.view.NameService
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation
import java.time.LocalDate

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
        when (interaction) {
            "action_build" -> {
                if (params.size < 2) {
                    throw Exception("Wrong number of parameters")
                }
                val building_id = position
                content = Building(building_id, params[1])
            }

            "action_crop" -> {
                val planter_id = params[0].toIntOrNull() ?: throw Exception("Invalid id")
                val newPlant =  Planter(planter_id, LocalDate.now())
                newPlant.plant(Crop(NameService.getDisplayName(params[1]), params[1]))
                content = newPlant
            }

            else -> {
                throw Exception("Unknown interaction")
            }
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