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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.szoftarch.farmgame.navigation.NavGraph
import hu.bme.aut.szoftarch.farmgame.ui.theme.FarmGameAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FarmGameAndroidTheme {
                NavGraph()
            }
        }
    }
}
