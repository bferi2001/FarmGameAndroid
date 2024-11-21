package hu.bme.aut.szoftarch.farmgame.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Map: Screen("map")
    object Market: Screen("market")
    object Quests: Screen("quests")
    object CreateAd: Screen("createAd")
}