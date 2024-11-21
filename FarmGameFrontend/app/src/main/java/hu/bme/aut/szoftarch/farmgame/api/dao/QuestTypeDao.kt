package hu.bme.aut.szoftarch.farmgame.api.dao

data class QuestTypeDao(
    var id: Int,
    var description: String,
    var rewardMoney: Int,
    var rewardXP: Int
)