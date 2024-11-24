package hu.bme.aut.szoftarch.farmgame.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler

import hu.bme.aut.szoftarch.farmgame.feature.login.LoginScreen
import hu.bme.aut.szoftarch.farmgame.feature.login.LoginViewModel
import hu.bme.aut.szoftarch.farmgame.feature.map.MapScreen
import hu.bme.aut.szoftarch.farmgame.feature.market.MarketScreen
import hu.bme.aut.szoftarch.farmgame.feature.market.createad.CreateAdScreen
import hu.bme.aut.szoftarch.farmgame.feature.quests.QuestsScreen
import kotlin.math.log


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onToMap = {
                    navController.navigate(Screen.Map.route)
                },
                loginViewModel = loginViewModel
            )
        }
        composable(Screen.Login.route + "/{message}") { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message")
            LoginScreen(
                onToMap = {
                    navController.navigate(Screen.Map.route)
                },
                loginViewModel = loginViewModel,
                message = message
            )
        }
        composable(Screen.Map.route) {
            MapScreen(
                onToLoginScreen = { message ->
                    loginViewModel.signOut()
                    if(message != null) {
                        navController.navigate(Screen.Login.route + "/$message")
                    } else {
                        navController.navigate(Screen.Login.route)
                    }
                },
                onToMarketScreen = {
                    navController.navigate(Screen.Market.route)
                },
                onToQuestsScreen = {
                    navController.navigate(Screen.Quests.route)
                },
            )
        }
        composable(Screen.Market.route) {
            MarketScreen(
                onToMap = {
                    navController.navigate(Screen.Map.route)
                },
                onToCreateAd = {
                    navController.navigate(Screen.CreateAd.route)
                },
            )
        }
        composable(Screen.Quests.route) {
            QuestsScreen (
                onToMap = {
                    navController.navigate(Screen.Map.route)
                }
            )
        }
        composable(Screen.CreateAd.route) {
            CreateAdScreen (
                onToMarketScreen = {
                    navController.navigate(Screen.Market.route)
                },
            )
        }
    }
}