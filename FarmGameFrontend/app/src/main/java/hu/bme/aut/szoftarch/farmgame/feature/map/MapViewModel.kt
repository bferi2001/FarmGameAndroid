package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hu.bme.aut.szoftarch.farmgame.data.Land

class MapViewModel constructor(
): ViewModel()
{
    var selectedLand by mutableStateOf<Land?>(null)
}