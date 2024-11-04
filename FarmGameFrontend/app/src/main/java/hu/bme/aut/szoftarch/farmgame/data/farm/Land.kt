package hu.bme.aut.szoftarch.farmgame.data.farm

import hu.bme.aut.szoftarch.farmgame.data.interaction.MenuLocation

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

    fun getInteractions(): List<String> {
        if (content != null) {
            return content!!.getInteractions()
        }
        return listOf("action_build", "action_plough")
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
     * action_plough:
     * param[0]: id of the planter
     */
    fun interact(interaction: String, params: List<String>) {
        if (content != null) {
            content!!.interact(interaction, params)
            return
        }
        when (interaction) {
            "action_build" -> {
                if (params.size < 2) {
                    throw Exception("Wrong number of parameters")
                }
                val building_id = params[0].toIntOrNull() ?: throw Exception("Invalid id")
                content = Building(building_id, params[1])
            }

            "action_plough" -> {
                val planter_id = params[0].toIntOrNull() ?: throw Exception("Invalid id")
                content = Planter(planter_id)
            }

            else -> {
                throw Exception("Unknown interaction")
            }
        }
    }

    fun getName(): String {
        return content?.GetName() ?: "Empty Field"
    }

    fun getTag(): String {
        return content?.GetTag() ?: "empty"
    }

}