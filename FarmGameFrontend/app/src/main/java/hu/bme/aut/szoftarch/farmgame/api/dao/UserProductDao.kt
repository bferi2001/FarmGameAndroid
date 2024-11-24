package hu.bme.aut.szoftarch.farmgame.api.dao

import kotlinx.serialization.Serializable

@Serializable
data class UserProductDao(
    var id: Int,
    var productName: String,
    var userName: String,
    var quantity: Int
)
