package hu.bme.aut.szoftarch.farmgame.api.dao

import kotlinx.serialization.Serializable

@Serializable
data class BarnDao(
    val id: Int,
    val userName: String,
    val typeName: String,
    val position: Int,
    val productionStartTime: String,
    val productionEndTime: String,
    val feedingTime: String,
    val cleaningTime: String,
    val level: Int
)
