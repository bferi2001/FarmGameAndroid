package hu.bme.aut.szoftarch.farmgame.feature.quests

class Quest(
    val goal: Int,
    val title: String,
    val description: String
) {
    var progress: Int = 0

    fun progress() {
        if (isClaimable()) return
        progress++
    }

    fun isClaimable(): Boolean {
        return progress >= goal
    }

}