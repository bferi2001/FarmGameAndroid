package hu.bme.aut.szoftarch.farmgame.feature.quests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.szoftarch.farmgame.api.ApiController
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestViewModel : ViewModel() {
    private val apiController = ApiController(LoginHandler.token!!)

    sealed class QuestLoadState{
        object Loading: QuestLoadState()
        data class Success(val quests: List<Quest>): QuestLoadState()
        data class Error(val message: String): QuestLoadState()
    }
    private var _loadingState = MutableStateFlow<QuestLoadState>(QuestLoadState.Loading)
    val loadingState = _loadingState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadQuests()
        }
    }
    suspend fun loadQuests() {
        try {
            val quests = apiController.getQuests()
            _loadingState.value = QuestLoadState.Success(quests)
        }
        catch (e: Exception){
            _loadingState.value = QuestLoadState.Error(e.message ?: "Something happened")
        }
    }
}

