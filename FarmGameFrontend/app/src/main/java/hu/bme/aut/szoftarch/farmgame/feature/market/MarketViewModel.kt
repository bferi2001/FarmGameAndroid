package hu.bme.aut.szoftarch.farmgame.feature.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.szoftarch.farmgame.api.ApiController
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler
import hu.bme.aut.szoftarch.farmgame.data.market.AdItemData
import hu.bme.aut.szoftarch.farmgame.data.market.SellingItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarketViewModel() : ViewModel() {

    sealed class LoadingState {
        data class Loaded(val items: List<AdItemData>, val inventory: List<SellingItemData>) : LoadingState()
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
        val apiController = ApiController(LoginHandler.token!!)
        val items = apiController.getAds()
        val inventory = apiController.getInventory()
        _loadingState.value = LoadingState.Loaded(items, inventory)
    }

    suspend fun createAd(newAd: SellingItemData) {
        val apiController = ApiController(LoginHandler.token!!)
        apiController.createAd(sellingItemData = newAd)
    }

    fun CreateNewAd(newAd: SellingItemData) {
        viewModelScope.launch(Dispatchers.IO) {
            createAd(newAd)
        }
    }

}