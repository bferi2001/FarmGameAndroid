package hu.bme.aut.szoftarch.farmgame.data.market

data class SellingItemData(
    val item: String,
    val price: Int,
    val count: Int,
    var sellCount: Int = 0,
)
