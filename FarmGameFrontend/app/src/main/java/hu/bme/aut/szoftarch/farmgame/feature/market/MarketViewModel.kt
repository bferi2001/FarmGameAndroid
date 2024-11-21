package hu.bme.aut.szoftarch.farmgame.feature.market

import androidx.lifecycle.ViewModel
import hu.bme.aut.szoftarch.farmgame.data.market.AdItemData
import hu.bme.aut.szoftarch.farmgame.data.market.SellingItemData

class MarketViewModel() : ViewModel() {

    val sellingItems = listOf(
        SellingItemData("Wheat", 1, 2),
        SellingItemData("Corn", 3, 4),
        SellingItemData("Carrot", 3000, 30),
        SellingItemData("EmptyTest", 0, 0),
    )

    val adItems = mutableListOf(
        AdItemData("Wheat", 5, 20, "TestUser1"),
        AdItemData("Corn", 10, 30, "test_user_2"),
        AdItemData("Wheat", 15, 20, "test_user_2"),
    )
}