package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.view.ImageService

const val FARM_GRID_COLUMN_COUNT = 10

@Composable
fun LandGrid(modifier: Modifier, viewModel: MapViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(FARM_GRID_COLUMN_COUNT),
        modifier = modifier
            .background(Color(0xFF2F8136))
    ) {
        items(viewModel.getFarm().lands) { land ->
            LandItem(land, viewModel)
        }
    }
}

@Composable
fun LandItem(land: Land, viewModel: MapViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(
                width = 2.dp,
                color =getBorderColor(land.position, viewModel.selectedLand),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp),
        onClick = {
            viewModel.onLandClicked(land)
        }
    ) {
        Image(
            painter = painterResource(id = ImageService.getImage(land.getTag())),
            contentDescription = land.getTag(),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}

fun getBorderColor(selected: Int, position: Int?): Color {
    return if (selected == position) {
        Color(0xFF004337)
    } else {
        Color.Transparent
    }
}

@Composable
fun CreateInteractButtons(viewModel: MapViewModel) {
    if (viewModel.selectedLand < 0) return Text(text = "invalid selection")

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
