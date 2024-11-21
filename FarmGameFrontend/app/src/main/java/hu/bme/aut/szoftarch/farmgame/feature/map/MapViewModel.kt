package hu.bme.aut.szoftarch.farmgame.feature.map

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.szoftarch.farmgame.api.DummyController
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler
import hu.bme.aut.szoftarch.farmgame.feature.game.Session
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val loginHandler: LoginHandler
) : ViewModel() {
    var selectedLand by mutableIntStateOf(-1)
    var menuOpen by mutableStateOf(MenuLocation.NONE)
        private set

    //TODO replace DummyController
    private val session = Session(DummyController())

    init {
        viewModelScope.launch {
            val token = ""
            session.initialize(token)
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