package hu.bme.aut.szoftarch.farmgame.feature.quests

data class Quest(
    val goal: Int,
    val title: String,
    val description: String,
    var progress: Int,
    val rewardMoney: Int,
    val rewardXP: Int
)