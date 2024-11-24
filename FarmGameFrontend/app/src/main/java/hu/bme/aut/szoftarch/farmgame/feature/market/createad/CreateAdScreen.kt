package hu.bme.aut.szoftarch.farmgame.feature.market.createad

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.compose.woodColor
import hu.bme.aut.szoftarch.farmgame.data.market.SellingItemData
import hu.bme.aut.szoftarch.farmgame.feature.market.MarketViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(
    marketViewModel: MarketViewModel = viewModel(),
    onToMarketScreen: () -> Unit
) {
    val context = LocalContext.current
    var inputValue by remember { mutableIntStateOf(0) }
    var inventory = remember { mutableStateListOf<SellingItemData>() } // State

    LaunchedEffect(key1 = marketViewModel.loadingState) {
        marketViewModel.loadingState.collect{
            when(it){
                is MarketViewModel.LoadingState.Loaded -> {
                    inventory.clear()
                    inventory.addAll(it.inventory)
                }
                is MarketViewModel.LoadingState.Loading -> {}
                is MarketViewModel.LoadingState.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
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
                            onClick = onToMarketScreen
                        ) {
                            Text(text = "Back to Market")
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
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                    //.background(woodColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // DropDown menu for sellable item selection
                        if (inventory.isNotEmpty())
                            DropDown(inventory)
                        else
                            Text(text = "There are no items")
                    }
                    Column {
                        // Input field with + and - buttons
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    inputValue = (inputValue - 1).coerceAtLeast(0)
                                    //onInputValueChanged(inputValue)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease")
                            }
                            TextField(
                                value = inputValue.toString(),
                                onValueChange = { newValue ->
                                    inputValue = newValue.toIntOrNull()?.coerceIn(0, 10) ?: 0
                                    //onInputValueChanged(inputValue)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(70.dp), // Set a specific width for the TextField
                            )
                            IconButton(
                                onClick = {
                                    inputValue = (inputValue + 1).coerceAtMost(10)
                                    //onInputValueChanged(inputValue)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Button(
                        enabled = inventory.isNotEmpty(),
                        onClick = {
                            /* TODO Create ad*/
                            onToMarketScreen()
                        }
                    ) {
                        Text("Create ad")
                    }
                }

            }
        }
    }
}

@Composable
fun DropDown(inventory: SnapshotStateList<SellingItemData>) {

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    val itemPosition = remember {
        mutableIntStateOf(0)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                Text(text = inventory[itemPosition.intValue].item)
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                inventory.forEachIndexed { index, data ->
                    DropdownMenuItem(text = {
                        Text(text = data.item)
                    },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.intValue = index
                        })
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun CreateAdScreenPreview() {
    AppTheme{
        CreateAdScreen(onToMarketScreen = {})
    }
}