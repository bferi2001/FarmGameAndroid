package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    onToLoginScreen: () -> Unit,
    onToMarketScreen: () -> Unit,
    onToQuestsScreen: () -> Unit,
) {
    var loading by remember{ mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = viewModel.loadingState) {
        viewModel.loadingState.collect{
            when(it){
                is MapViewModel.InitState.Success -> {
                    loading = false
                }
                is MapViewModel.InitState.Error -> {
                    onToLoginScreen()
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
                            onClick = onToLoginScreen
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
        if(loading)
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        else{
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
}



@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    AppTheme{
        MapScreen(onToLoginScreen = {}, onToMarketScreen = {}, onToQuestsScreen = {})
    }
}