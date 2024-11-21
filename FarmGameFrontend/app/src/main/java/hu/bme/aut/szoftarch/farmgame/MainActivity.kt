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
import com.example.compose.AppTheme
import hu.bme.aut.szoftarch.farmgame.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                NavGraph()
            }
        }
    }
}
