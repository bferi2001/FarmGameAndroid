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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.compose.earthTone
import com.example.compose.woodLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestsScreen(
    questsViewModel: QuestViewModel = viewModel(),
    onToMap: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var loading by remember { mutableStateOf(true) }
    var quests by remember { mutableStateOf(listOf<Quest>()) }

    LaunchedEffect(key1 = questsViewModel.loadingState) {
        questsViewModel.loadingState.collect{
            when(it){
                is QuestViewModel.QuestLoadState.Loading -> {
                    loading = true
                }
                is QuestViewModel.QuestLoadState.Success -> {
                    loading = false
                    quests = it.quests
                }
                is QuestViewModel.QuestLoadState.Error -> {
                    loading = false
                    snackbarHostState.showSnackbar(it.message, duration = SnackbarDuration.Short)
                }
            }
        }
    }

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        if(loading)
        {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        else
        {
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
                Text(text = "Progress: ${quest.progress}/${quest.goal}")
                Column{
                    Text(text = "Reward money: ${quest.rewardMoney}")
                    Text(text = "Reward xp: ${quest.rewardXP}")
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun QuestsScreenPreview() {
    AppTheme{
        QuestsScreen(onToMap = {})
    }
}