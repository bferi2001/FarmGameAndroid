package hu.bme.aut.szoftarch.farmgame.view

import hu.bme.aut.szoftarch.farmgame.api.DummyController

object NameService {
    val controllers = DummyController()

    var displayNames: Map<String, String>

    init {
        displayNames = controllers.getDisplayNames()
    }

    fun getDisplayName(tag: String): String {
        return displayNames.getOrDefault(tag, tag)
    }
}