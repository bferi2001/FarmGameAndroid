package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hu.bme.aut.szoftarch.farmgame.data.farm.Land
import hu.bme.aut.szoftarch.farmgame.data.game.Session

class MapViewModel constructor(
): ViewModel()
{
    var selectedLand by mutableStateOf<Land?>(null)
    var showBottomBar by mutableStateOf(false)

    //val session: Session = Session()

    init {

    }
}