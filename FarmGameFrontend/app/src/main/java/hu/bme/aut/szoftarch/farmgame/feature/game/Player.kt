package hu.bme.aut.szoftarch.farmgame.feature.game

data class Player(
    val id: Int,
    val resources: MutableMap<String, Int>,
    var xp: Int,
    var level: Int,
) {


}