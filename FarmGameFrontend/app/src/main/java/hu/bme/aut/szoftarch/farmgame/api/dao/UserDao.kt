package hu.bme.aut.szoftarch.farmgame.api.dao

data class UserDao(
    var id: Int,
    var email: String,
    var userXP: Int,
    var userMoney: Int
)
