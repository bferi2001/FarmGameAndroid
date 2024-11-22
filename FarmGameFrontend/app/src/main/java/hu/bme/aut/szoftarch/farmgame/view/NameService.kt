package hu.bme.aut.szoftarch.farmgame.view

import hu.bme.aut.szoftarch.farmgame.api.ApiController
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler

object NameService {
    val controllers = ApiController(LoginHandler.token!!)

    var displayNames: Map<String, String> = controllers.getDisplayNames()

    fun getDisplayName(tag: String): String {
        return displayNames.getOrDefault(tag, tag)
    }
}