package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.szoftarch.farmgame.api.ApiController
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler
import hu.bme.aut.szoftarch.farmgame.feature.game.Session
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Farm
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Planter
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.User
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
    var selectedLandId = MutableStateFlow(-1)
    var menuOpen by mutableStateOf(MenuLocation.NONE)
        private set

    lateinit var session: Session

    sealed class InitState{
        object Loading: InitState()
        data class Success(val farm: Farm, val user: User): InitState()
        data class Error(val message: String): InitState()
    }
    private var _loadingState = MutableStateFlow<InitState>(InitState.Loading)
    val loadingState = _loadingState.asStateFlow()

    init {
        load()
        fetchInteractions()
    }

    fun load(){
        _loadingState.value = InitState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                session = Session(ApiController(LoginHandler.token!!))
                session.initialize()
                val farm = session.farm
                if(farm == null)
                {
                    throw Exception("Couldn't initialize the farm")
                }
                if(session.user == null)
                {
                    throw Exception("Couldn't initialize the user")
                }
                _loadingState.value = InitState.Success(farm, session.user!!)
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
        interactions.value = emptyList()
        selectedLandId.value = land.position
        menuOpen = land.getInteractMenu()
    }

    fun interact(interaction: String, params: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (selectedLandId.value < 0) {
                return@launch
            }
            if(session.apiController.interact(session.farm!!.getLand(selectedLandId.value), interaction, params)){
                closeMenu()
                selectedLandId.value = -1

                if(interaction == "action_crop" || interaction == "harvesting")
                {
                    load()
                }
            }

            // Quick way to update UI
            val cheese = selectedLandId.value
            selectedLandId.value = -1
            selectedLandId.value = cheese
        }
    }

    val interactions: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private fun fetchInteractions(){
        viewModelScope.launch(Dispatchers.IO) {
            while (true)
            {
                var newInteractions = emptyList<String>()
                if (selectedLandId.value >= 0) {
                    try{
                        val selectedId = selectedLandId.value
                        val isPlant = session.farm!!.getLand(selectedId).content is Planter
                        val actions = session.apiController.getActions(selectedId, isPlant)
                        session.farm!!.getLand(selectedId).content?.setNewActions(actions)
                        newInteractions = session.getInteractions(session.farm!!.getLand(selectedId))
                    }
                    catch (e: Exception){
                        newInteractions = emptyList()
                    }
                }

                if(newInteractions != interactions.value){
                    interactions.value = newInteractions
                }

                delay(1000)
            }
        }
    }

    fun getSelectedLandById(id: Int) : Land? {
        if (selectedLandId.value < 0) {
            return null
        }
        return session.farm?.getLand(selectedLandId.value)
    }
}