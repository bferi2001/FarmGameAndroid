package hu.bme.aut.szoftarch.farmgame.api.dao

import kotlinx.serialization.Serializable

@Serializable
data class QuestDao(
    var id: Int,
    var userName: String,
    var taskKeyword: String,
    var objectId: String,
    var goalQuantity: Int,
    var currentQuantity: Int,
    var rewardMoney: Int,
    var rewardXP: Int
)