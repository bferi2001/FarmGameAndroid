package hu.bme.aut.szoftarch.farmgame.api.dao

import java.time.LocalDateTime

data class ClassifiedDao(
    var id: Int,
    var userName: String? = null,
    var price: Int,
    var productName: String,
    var deadline: LocalDateTime,
    var quantity: Int
)
