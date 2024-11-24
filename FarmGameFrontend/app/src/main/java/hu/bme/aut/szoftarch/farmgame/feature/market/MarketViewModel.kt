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
    private val apiController = ApiController(LoginHandler.token!!)

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

    private suspend fun loadItems() {
        val items = apiController.getAds()
        val inventory = apiController.getInventory()
        _loadingState.value = LoadingState.Loaded(items, inventory)
    }

    fun createNewAd(newAd: SellingItemData) {
        viewModelScope.launch(Dispatchers.IO) {
            apiController.createAd(sellingItemData = newAd)
            loadItems()
        }
    }

    fun buyAd(adId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            apiController.buyAd(adId)
            loadItems()
        }
    }

    fun QuickSell(item: SellingItemData) {
        viewModelScope.launch(Dispatchers.IO) {
            apiController.quickSell(sellingItemData = item)
            loadItems()
        }
    }

}