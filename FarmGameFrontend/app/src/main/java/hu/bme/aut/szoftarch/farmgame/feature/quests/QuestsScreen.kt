package hu.bme.aut.szoftarch.farmgame.feature.quests

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.compose.earthTone
import com.example.compose.woodLight

val viewModel = QuestViewModel()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestsScreen(
    onToMap: () -> Unit,
) {
    val quests by viewModel.quests.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.border(4.dp, earthTone),
                colors = topAppBarColors(
                    containerColor = woodLight,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = onToMap
                        ) {
                            Text(text = "Back to Map")
                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
            ) {
                items(quests) { quest ->
                    QuestItem(quest)
                }
            }
        }
    }
}

@Composable
fun QuestItem(quest: Quest) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = quest.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = quest.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (quest.progress.toFloat() / quest.goal.toFloat()).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray),
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            )
            {
                if (!quest.isClaimable()) {
                    Button(
                        onClick = {
                            quest.progress()
                        },
                    ) {
                        Text("Progress")
                    }
                }
                Text(text = "${quest.progress}/${quest.goal}")
                Button(
                    onClick = {
                        viewModel.claimQuest(quest)
                    },
                    enabled = quest.isClaimable(),
                ) {
                    Text("Claim")
                }
            }
        }
    }
}