package hu.bme.aut.szoftarch.farmgame.data.market

import java.time.LocalDateTime

data class AdItemData(
    val id: Int,
    val item: String,
    val price: Int,
    val count: Int,
    val seller: String,
    val deadline: LocalDateTime? = null
)