package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.viewmodel.compose.viewModel
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
                    onToLoginScreen
                    loading = false
                }
                else -> loading = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    BottomMenuContent(viewModel)
                }
            }
        },
    ) { innerPadding ->
        if(loading)
        {
            CircularProgressIndicator()
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




