package hu.bme.aut.szoftarch.farmgame.view

import androidx.compose.ui.graphics.Color
import hu.bme.aut.szoftarch.farmgame.api.DummyController

object ImageService {
    val controllers = DummyController()
    var images = mutableMapOf<String, Color>()

    //TODO replace with image current implenmentation only usees colors
    init {
        images = controllers.getImages()
    }

    fun getImage(tag: String): Color {
        return images.getOrDefault(tag, Color.Gray)
    }
}