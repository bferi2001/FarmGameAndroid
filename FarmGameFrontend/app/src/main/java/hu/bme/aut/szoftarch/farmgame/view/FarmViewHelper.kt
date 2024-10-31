package hu.bme.aut.szoftarch.farmgame.view

import androidx.compose.ui.graphics.Color
import hu.bme.aut.szoftarch.farmgame.data.farm.Land

class FarmViewHelper {
    fun GetLandColor(land: Land): Color {
        return when (land.GetTag()){
            "wheat" -> Color(0xFFF8B423)
            "cows" -> Color(0xFF964B00)
            "flowers" -> Color(0xFF7F00FF)
            "empty" -> Color(0xFF3f9b0b)
/*
            "crop_wheat" -> Color(0xFFF8B423)
            "crop_flowers" -> Color(0xFF7F00FF)
            "building_cow_shed" -> Color(0xFF964B00)
            "crop_null" -> Color.DarkGray
            "empty" -> Color(0xFF3f9b0b)
*/
            else -> Color.Gray
        }
    }
}