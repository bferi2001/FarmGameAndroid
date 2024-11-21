package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.szoftarch.farmgame.api.DummyController
import hu.bme.aut.szoftarch.farmgame.feature.game.Session
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation
import kotlinx.coroutines.launch

class MapViewModel(
    val playerId: Int,
) : ViewModel() {
    var selectedLand by mutableIntStateOf(-1)
    var menuOpen by mutableStateOf(MenuLocation.NONE)
        private set

    //TODO replace DummyController
    private val session = Session(playerId, DummyController())

    init {
        viewModelScope.launch {
            session.initialize()
        }
    }

    fun openMenu(menuLocation: MenuLocation) {
        menuOpen = menuLocation
    }

    fun closeMenu() {
        menuOpen = MenuLocation.NONE
    }

    fun getFarm(): Farm {
        return session.farm
    }

    fun onLandClicked(land: Land) {
        selectedLand = land.position
        menuOpen = land.getInteractMenu()
    }

    fun interact(interaction: String, params: List<String>) {
        if (selectedLand < 0) {
            return
        }
        session.controller.interact(session.farm.getLand(selectedLand), interaction, params)
    }

    fun getInteractions(): List<String> {
        if (selectedLand < 0) {
            return emptyList()
        }
        return session.getInteractions(session.farm.getLand(selectedLand))
    }


}