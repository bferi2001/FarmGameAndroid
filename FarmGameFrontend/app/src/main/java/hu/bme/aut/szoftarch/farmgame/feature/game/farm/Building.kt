package hu.bme.aut.szoftarch.farmgame.feature.game.farm

import hu.bme.aut.szoftarch.farmgame.view.NameService
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation

class Building(
    val id: Int,
    private val tag: String,
) : Buildable() {
    var level: Int = 0
    val maxLevel: Int = 3

    var processing: Boolean = false
    var clean: Boolean = true

    override fun getName(): String {
        return "${NameService.getDisplayName(tag)} lvl${level}"
    }
    override fun getTag(): String {
        return if (clean) {
            tag
        } else {
            "${tag}_dirty"
        }
    }

    override fun getInteractMenu(): MenuLocation {
        return MenuLocation.SIDE
    }

    override fun getInteractions(): List<String> {
        val interactions = mutableListOf<String>()
        if (processing) {
            interactions.add("wait")
        } else {
            if (level < maxLevel) {
                interactions.add("upgrade")
            }
            interactions.add("start")
        }
        if (!clean) {
            interactions.add("clean")
        }
        return interactions
    }

    override fun interact(interaction: String, params: List<String>) {
        when (interaction) {
            "start" -> {
                processing = true
                clean = false
            }

            "wait" -> {
                if (clean) {
                    processing = false
                } else {
                    clean = false
                }
            }

            "clean" -> {
                clean = true
            }

            "upgrade" -> {
                level++
            }
        }
    }
}