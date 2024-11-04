package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.bme.aut.szoftarch.farmgame.data.farm.Land
import hu.bme.aut.szoftarch.farmgame.data.interaction.MenuLocation
import hu.bme.aut.szoftarch.farmgame.view.FarmViewHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onToMapDebug: () -> Unit,
) {
    val viewModel = MapViewModel()

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
                            onClick = onToMapDebug
                        ) {
                            Text(text = "Log out")
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "interact menu",
                        )
                        CreateInteractButtons(viewModel.selectedLand, viewModel)

                        Button(
                            onClick = {
                                viewModel.menuOpen = MenuLocation.NONE
                            }
                        ) {
                            Text(text = "Close")
                        }
                    }
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

@Composable
fun LandGrid(modifier: Modifier, viewModel: MapViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(viewModel.farm.columns),
        modifier = modifier
            .height(400.dp)
            .background(Color(0xFFa5d62c))
    ) {
        items(viewModel.farm.lands) { land ->
            LandItem(land, viewModel)
        }
    }
}

@Composable
fun LandItem(land: Land, viewModel: MapViewModel) {
    val farmViewHelper = FarmViewHelper()
    Card(
        colors = CardColors(
            containerColor = farmViewHelper.GetLandColor(land),
            contentColor = Color.Black,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.Gray
        ),
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .border(1.dp, getBorderColor(land.position, viewModel.selectedLand?.position)),
        onClick = {
            onLandClicked(land, viewModel)
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "id " + land.id)
            Text(text = land.getName())
        }
    }
}

fun getBorderColor(selected: Int, position: Int?): Color {
    return if (selected == position) {
        Color.Black
    } else {
        Color.Transparent
    }
}

fun onLandClicked(land: Land, viewModel: MapViewModel) {
    viewModel.selectedLand = land
    viewModel.menuOpen = land.getInteractMenu()
}

@Composable
fun CreateInteractButtons(land: Land?, viewModel: MapViewModel) {
    if (land == null) return Text(text = "invalid selection")

    land.getInteractions().forEach { interaction ->
        Button(
            onClick = {
                land.interact(
                    interaction, /*Temp placeholder -> */
                    listOf("1234", "building_cow_shed")
                )
                viewModel.menuOpen = MenuLocation.NONE
                viewModel.selectedLand = null
            })
        {
            Text(text = interaction)
        }
    }
}

@Composable
fun SideMenuScreen(viewModel: MapViewModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val menuOffsetX by animateDpAsState(
        targetValue = if (viewModel.menuOpen != MenuLocation.SIDE) -(screenWidth / 2) else 0.dp
    )

    Box(
        modifier = Modifier
            .offset(menuOffsetX)
            .fillMaxHeight()
            .background(Color(0xFFe8bf8a))
            .width(screenWidth / 2)

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(text = "Side menu")
            CreateInteractButtons(viewModel.selectedLand, viewModel)
            Button(
                onClick = { viewModel.menuOpen = MenuLocation.NONE }
            ) {
                Text(text = "Close side")
            }
        }
    }
}