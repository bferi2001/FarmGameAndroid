package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.szoftarch.farmgame.api.DummyController
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler
import hu.bme.aut.szoftarch.farmgame.feature.game.Session
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
    var selectedLand by mutableIntStateOf(-1)
    var menuOpen by mutableStateOf(MenuLocation.NONE)
        private set

    //TODO replace DummyController
    private val session = Session(DummyController())

    sealed class InitState{
        object Loading: InitState()
        data class Success(val farm: Farm): InitState()
        data class Error(val message: String): InitState()
    }
    private var _loadingState = MutableStateFlow<InitState>(InitState.Loading)
    val loadingState = _loadingState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                session.initialize(LoginHandler.token!!)
                val farm = session.farm
                if(farm == null)
                {
                    throw Exception("Couldn't initialize the farm")
                }
                _loadingState.value = InitState.Success(farm)
            }
            catch (e: Exception){
                _loadingState.value = InitState.Error(e.message ?: "Something happened")
            }
        }
    }

    fun openMenu(menuLocation: MenuLocation) {
        menuOpen = menuLocation
    }

    fun closeMenu() {
        menuOpen = MenuLocation.NONE
    }

    fun getFarm(): Farm? {
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
        session.controller.interact(session.farm!!.getLand(selectedLand), interaction, params)
    }

    fun getInteractions(): List<String> {
        if (selectedLand < 0) {
            return emptyList()
        }
        return session.getInteractions(session.farm!!.getLand(selectedLand))
    }


}