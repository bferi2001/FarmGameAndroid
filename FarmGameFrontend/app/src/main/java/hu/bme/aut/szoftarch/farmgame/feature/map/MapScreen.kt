package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.bme.aut.szoftarch.farmgame.data.farm.Farm
import hu.bme.aut.szoftarch.farmgame.data.farm.Land
import hu.bme.aut.szoftarch.farmgame.view.FarmViewHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen( onToMapDebug: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scope = currentRecomposeScope

    val viewModel = MapViewModel()

    val columns= 10
    val rows= 10

    val farm = remember { Farm(columns,rows) }

    LaunchedEffect(Unit) {
        farm.AddLand(Land(0,4,"wheat"))
        farm.AddLand(Land(0,7,"cows"))
        farm.AddLand(Land(0,3,"flowers"))
        farm.AddLand(Land(0,34,"flowers"))

        /*
        farm.AddLand(Land(0,4, Planter(0)))
        farm.AddLand(Land(0,7,CowShed(0,0)))
        farm.AddLand(Land(0,3,CowShed(0,0)))
        farm.AddLand(Land(0,34,Planter(0)))
        */
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
                        onClick = onToMapDebug
                    ) {
                        Text(text = "Log out")
                    }
                    Button(
                        onClick = {viewModel.showBottomBar = true}
                    ) {
                        Text(text = "Open bottom")
                    }
                }
            }
        )
    },
    bottomBar = {
        AnimatedVisibility(visible = viewModel.showBottomBar) {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        textAlign = TextAlign.Center,
                        text = "interract menu",
                    )
                    Button(onClick =
                    {
                        viewModel.selectedLand?.content = "wheat"
                        scope.invalidate()
                        viewModel.showBottomBar = false
                    }){
                        Text(text = "Wheat")
                    }
                    Button(onClick = {
                        viewModel.selectedLand?.content = "flowers"
                        scope.invalidate()
                        viewModel.showBottomBar = false
                    }){
                        Text(text = "Flowers")
                    }
                    Button(
                        onClick = {
                            viewModel.showBottomBar = false
                        }
                    ){
                        Text(text = "Close")
                    }
                }
            }
        }
    },
    ) { innerPadding ->
        LandGrid(farm = farm, Modifier.padding(innerPadding), viewModel)
    }
}

@Composable
fun LandGrid(farm: Farm, modifier: Modifier, viewModel: MapViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(farm.columns),
        modifier = modifier.height(400.dp)
    ) {
        items(farm.lands) { land ->
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
            disabledContainerColor = Color.Gray),
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .border(1.dp,getBorderColor(land.position, viewModel.selectedLand?.position)),
        onClick = {
            viewModel.selectedLand = land
            viewModel.showBottomBar = true
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "id "+land.id)
            Text(text = land.content)
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