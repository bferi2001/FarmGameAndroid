package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation

class MapViewModel(
) : ViewModel() {
    var selectedLand by mutableStateOf<Land?>(null)
    var menuOpen by mutableStateOf(MenuLocation.NONE)

    val columns = 10
    val rows = 10

    val farm = Farm(columns, rows)

    //val session: Session = Session()
    init {

    }
}