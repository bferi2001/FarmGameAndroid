package hu.bme.aut.szoftarch.farmgame.data.market

data class SellingItemData(
    val item: String,
    val price: Int,
    var quantity: Int = 0,
    var sellCount: Int = 0,
)
