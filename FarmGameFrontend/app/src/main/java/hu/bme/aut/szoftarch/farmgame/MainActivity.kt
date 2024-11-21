//Házi feladat - "Én kicsi farmom" alkalmazás
//Szoftverarchitektúrák - 2024/őszi félév
//Készítették:      - Barna Ferenc (Y98NYK)
//                  - Csikós Patrik (E4MZUV)
//                  - Török Gergely Balázs (JHP4SD)
//                  - Tuskó László István (CGTRRV)

package hu.bme.aut.szoftarch.farmgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.szoftarch.farmgame.feature.login.LoginViewModel
import hu.bme.aut.szoftarch.farmgame.navigation.NavGraph
import hu.bme.aut.szoftarch.farmgame.ui.theme.FarmGameAndroidTheme
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FarmGameAndroidTheme {
                NavGraph(loginViewModel = loginViewModel)
            }
        }
    }
}
