package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.view.ImageService

@Composable
fun LandGrid(modifier: Modifier, viewModel: MapViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(viewModel.getFarm().columns),
        modifier = modifier
            .height(400.dp)
            .background(Color(0xFFa5d62c))
    ) {
        items(viewModel.getFarm().lands) { land ->
            LandItem(land, viewModel)
        }
    }
}

@Composable
fun LandItem(land: Land, viewModel: MapViewModel) {
    Card(
        colors = CardColors(
            containerColor = ImageService.getImage(land.getTag()),
            contentColor = Color.Black,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.Gray
        ),
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .border(1.dp, getBorderColor(land.position, viewModel.selectedLand)),
        onClick = {
            viewModel.onLandClicked(land)
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

@Composable
fun CreateInteractButtons(viewModel: MapViewModel) {
    if (viewModel.selectedLand < 1) return Text(text = "invalid selection")

    viewModel.getInteractions().forEach { interaction ->
        Button(
            onClick = {
                viewModel.interact(
                    interaction, /*Temp placeholder -> */
                    listOf("1234", "building_cow_shed")
                )
                viewModel.closeMenu()
                viewModel.selectedLand = -1
            })
        {
            Text(text = interaction)
        }
    }
}
