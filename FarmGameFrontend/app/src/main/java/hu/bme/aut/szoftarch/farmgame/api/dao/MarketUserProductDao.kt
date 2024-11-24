package hu.bme.aut.szoftarch.farmgame.api.dao

import kotlinx.serialization.Serializable

@Serializable
data class MarketUserProductDao(
    var productName: String,
    var quickSellPrice: Int,
    var quantity: Int
)
