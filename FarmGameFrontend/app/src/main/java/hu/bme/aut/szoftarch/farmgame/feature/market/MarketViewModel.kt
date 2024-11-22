package hu.bme.aut.szoftarch.farmgame.feature.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.szoftarch.farmgame.api.Controller
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler
import hu.bme.aut.szoftarch.farmgame.data.market.AdItemData
import hu.bme.aut.szoftarch.farmgame.data.market.SellingItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarketViewModel() : ViewModel() {

    val sellingItems = listOf(
        SellingItemData("Wheat", 1, 2),
        SellingItemData("Corn", 3, 4),
        SellingItemData("Carrot", 3000, 30),
        SellingItemData("EmptyTest", 0, 0),
    )

    sealed class LoadingState {
        data class Loaded(val items: List<AdItemData>) : LoadingState()
        object Loading : LoadingState()
        data class Error(val message: String) : LoadingState()
    }
    private var _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState = _loadingState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadItems()
            } catch (e: Exception) {
                _loadingState.value = LoadingState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun loadItems() {
        val controller = Controller(LoginHandler.token!!)
        val items = controller.getAds()
        _loadingState.value = LoadingState.Loaded(items)
    }

}