package hu.bme.aut.szoftarch.farmgame.view

import androidx.compose.ui.graphics.Color
import hu.bme.aut.szoftarch.farmgame.data.Land

class FarmViewHelper {
    fun GetLandColor(land: Land): Color {
        return when (land.content){
            "wheat" -> Color(0xFFF8B423)
            "cows" -> Color(0xFF964B00)
            "flowers" -> Color(0xFF7F00FF)
            "empty" -> Color(0xFF3f9b0b)
            else -> Color.Gray
        }
    }
}