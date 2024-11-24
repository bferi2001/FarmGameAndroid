package hu.bme.aut.szoftarch.farmgame.api.dao

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ClassifiedDao(
    var id: Int,
    var userName: String? = null,
    var price: Int,
    var productName: String,
    var deadline: String,
    var quantity: Int
)
