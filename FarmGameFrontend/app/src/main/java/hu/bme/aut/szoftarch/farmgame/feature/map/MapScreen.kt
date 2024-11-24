package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.compose.earthTone
import com.example.compose.woodColor
import com.example.compose.woodLight
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel(),
    onToLoginScreen: (message: String?) -> Unit,
    onToMarketScreen: () -> Unit,
    onToQuestsScreen: () -> Unit,
) {
    var loading by remember{ mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    var userXp by remember { mutableIntStateOf(0) }
    var userMoney by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = viewModel.loadingState) {
        viewModel.loadingState.collect{
            when(it){
                is MapViewModel.InitState.Success -> {
                    loading = false
                    userXp = it.user.userXP
                    userMoney = it.user.userMoney
                }
                is MapViewModel.InitState.Error -> {
                    onToLoginScreen(it.message)
                    loading = false
                }
                else -> loading = true
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier.border(4.dp, earthTone),
                colors = topAppBarColors(
                    containerColor = woodColor,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = {onToLoginScreen(null)}
                        ) {
                            Text(text = "Log out")
                        }
                        Button(
                            onClick = onToMarketScreen
                        ) {
                            Text(text = "Market")
                        }
                        Button(
                            onClick = onToQuestsScreen
                        ) {
                            Text(text = "Quests")
                        }
                        if(loading){
                            Spacer(modifier = Modifier.weight(1f))
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "XP: $userXp", modifier = Modifier.padding(end = 8.dp))
                        Text(text = "Money: $userMoney", modifier = Modifier.padding(end = 8.dp))
                    }
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(visible = viewModel.menuOpen == MenuLocation.BOTTOM) {
                BottomAppBar(
                    containerColor = woodLight,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.border(4.dp, earthTone),
                ) {
                    BottomMenuContent(viewModel)
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LandGrid(
                    Modifier
                        .fillMaxSize(),
                    viewModel
                )
            }
            SideMenuScreen(viewModel)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    AppTheme{
        MapScreen(onToLoginScreen = {}, onToMarketScreen = {}, onToQuestsScreen = {})
    }
}