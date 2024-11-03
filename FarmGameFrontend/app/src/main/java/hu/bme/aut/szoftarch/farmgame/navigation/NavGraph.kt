package hu.bme.aut.szoftarch.farmgame.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import hu.bme.aut.szoftarch.farmgame.feature.login.LoginScreen
import hu.bme.aut.szoftarch.farmgame.feature.map.MapScreen


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onToMapDebug = {
                    navController.navigate(Screen.Map.route)
                }
            )
        }
        composable(Screen.Map.route) {
            MapScreen(
                onToMapDebug = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
    }
}