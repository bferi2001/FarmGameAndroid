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
import hu.bme.aut.szoftarch.farmgame.R
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.view.ImageService
import hu.bme.aut.szoftarch.farmgame.view.NameService
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.text.toFloat

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
                color = getBorderColor(land.position, viewModel.selectedLand),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp),
        onClick = {
            viewModel.onLandClicked(land)
        }
    ) {
        Box() {
            val image = ImageService.getImage(land.getTag())
            Image(
                painter = painterResource(id = image),
                contentDescription = land.getTag(),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            if (image == R.drawable.missing_tile) {
                Text(text = land.getName())
            }
        }
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
                    interaction.split(":")[0], /*Temp placeholder -> */
                    listOf("1234", interaction.split(":").getOrNull(1) ?: "")
                )
                viewModel.closeMenu()
                viewModel.selectedLand = -1
            })
        {
            Text(text = NameService.getDisplayName(interaction))
        }
    }
}
