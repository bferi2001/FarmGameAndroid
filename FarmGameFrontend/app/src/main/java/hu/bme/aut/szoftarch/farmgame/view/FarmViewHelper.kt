package hu.bme.aut.szoftarch.farmgame.view

import androidx.compose.ui.graphics.Color
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land

class FarmViewHelper {
    fun GetLandColor(land: Land): Color {
        return when (land.getTag()){
            "crop_wheat" -> Color(0xFFF8B423)
            "crop_flowers" -> Color(0xFF7F00FF)
            "building_cow_shed" -> Color.Gray
            "crop_null" -> Color(0xFF964B00)
            "empty" -> Color(0xFF3f9b0b)

            else -> Color.White
        }
    }
}