package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.bme.aut.szoftarch.farmgame.R
import hu.bme.aut.szoftarch.farmgame.feature.game.farm.Land
import hu.bme.aut.szoftarch.farmgame.view.ImageService
import hu.bme.aut.szoftarch.farmgame.view.NameService
import kotlinx.coroutines.delay
import java.util.Date

const val FARM_GRID_COLUMN_COUNT = 10

@Composable
fun LandGrid(modifier: Modifier, viewModel: MapViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(FARM_GRID_COLUMN_COUNT),
        modifier = modifier
            .background(Color(0xFF2F8136)) // Texture background Color
    ) {
        items(viewModel.getFarm()?.lands ?: emptyList<Land>()) { land ->
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

@Composable
fun getBorderColor(selected: Int, position: Int?): Color {
    return if (selected == position) {
        MaterialTheme.colorScheme.outline
    } else {
        Color.Transparent
    }
}

@Composable
fun CreateInteractButtons(viewModel: MapViewModel) {
    if (viewModel.selectedLand < 0) return Text(text = "invalid selection")
    if (viewModel.getSelectedLand()?.isProcessing() == true) {
        CountdownProgressBar(
            viewModel.getSelectedLand()?.content?.getTargetDate() ?: Date(),
            viewModel.getSelectedLand()?.content?.getStartDate() ?: Date()
        )
    }

    var interactions = remember { mutableStateListOf<String>() }
    LaunchedEffect(key1 = viewModel.interactions) {
        viewModel.interactions.collect{ it ->
            interactions.clear()
            interactions.addAll(it)
        }
    }
    interactions.forEach { interaction ->
        Button(
            onClick = {
                viewModel.interact(
                    interaction.split(":")[0], /*Temp placeholder -> */
                    listOf("1234", interaction.split(":").getOrNull(1) ?: "")
                )
            })
        {
            Text(text = NameService.getDisplayName(interaction.split(":").getOrNull(1) ?: interaction))
        }
    }
}


@Composable
fun CountdownProgressBar(targetDate: Date, startingTime: Date) {
    var remainingDuration by remember { mutableLongStateOf(0L) }
    var currentTargetDate by remember { mutableLongStateOf(targetDate.time) }

    val totalDuration = targetDate.time - startingTime.time

    LaunchedEffect(key1 = currentTargetDate) {
        while (true) {
            remainingDuration = currentTargetDate - Date().time
            if (remainingDuration <= 0) break // Stop when target time is reached
            delay(1000) // Update every second
        }
    }

    Column(horizontalAlignment = Alignment.End) {
        LinearProgressIndicator(
            progress = { 1f-(remainingDuration.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f) },
            color = Color.Green,
        )
        Text(if(remainingDuration < 0L) "Time's up!" else formatRemainingTime(remainingDuration))
    }
}

fun formatRemainingTime(millis: Long): String {
    val days = millis / (1000 * 60 * 60 * 24)
    val hours = (millis % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
    val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (millis % (1000 * 60)) / 1000

    return when {
        days > 0 -> {
            "${days} and ${leadZero(hours)}:${leadZero(minutes)}:${leadZero(seconds)} remaining"
        }
        hours > 0 -> {
            "${hours}:${leadZero(minutes)}:${leadZero(seconds)} remaining"
        }
        minutes > 0 -> {
            "${minutes}:${leadZero(seconds)} remaining"
        }
        seconds > 0 -> {
            "00:${leadZero(seconds)} remaining"
        }
        else -> {
            "invalid"
        }
    }
}

fun leadZero(integer: Long): String {
    return integer.toString().padStart(2, '0')
}