package hu.bme.aut.szoftarch.farmgame.feature.market.createad

import android.R.attr.text
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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import hu.bme.aut.szoftarch.farmgame.data.market.SellingItemData
import hu.bme.aut.szoftarch.farmgame.feature.market.MarketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(
    marketViewModel: MarketViewModel = viewModel(),
    onToMarketScreen: () -> Unit
) {
    val context = LocalContext.current
    var inputValueQuantity by remember { mutableIntStateOf(0) }
    var inputValuePrice by remember { mutableIntStateOf(0) }
    var inventory = remember { mutableStateListOf<SellingItemData>() } // State
    var selectedItem by remember { mutableStateOf("") }
    var maxQuantity by remember { mutableIntStateOf(0) }

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
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row (
                            modifier = Modifier
                                .height(150.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // DropDown menu for sellable item selection
                            if (inventory.isNotEmpty()) {
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

                                                        selectedItem = data.item
                                                        maxQuantity = data.quantity
                                                    })
                                            }
                                        }
                                    }

                                }
                            }
                            else
                                Text(text = "There are no items")
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Column (
                            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Text(
                                text = "Quantity",
                                fontWeight = FontWeight.Bold)
                            // Input field with + and - buttons
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        inputValueQuantity = (inputValueQuantity - 1).coerceAtLeast(0)
                                    },
                                    modifier = Modifier
                                        .width(100.dp)
                                ) {
                                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease")
                                }
                                TextField(
                                    value = inputValueQuantity.toString(),
                                    onValueChange = { newValue ->
                                        inputValueQuantity = newValue.toIntOrNull()?.coerceIn(0, maxQuantity) ?: 0
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(100.dp), // Set a specific width for the TextField
                                )
                                IconButton(
                                    onClick = {
                                        inputValueQuantity = (inputValueQuantity + 1).coerceAtMost(maxQuantity)
                                    },
                                    modifier = Modifier
                                        .width(100.dp)
                                ) {
                                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Column (
                            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                        )  {
                            Text(text = "Price",
                                fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        inputValuePrice = (inputValuePrice - 1).coerceAtLeast(0)
                                    },
                                    modifier = Modifier
                                        .width(100.dp)
                                ) {
                                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease")
                                }
                                TextField(
                                    value = inputValuePrice.toString(),
                                    onValueChange = { newValue ->
                                        inputValuePrice = newValue.toIntOrNull()?.coerceAtLeast(0) ?: 0
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(100.dp), // Set a specific width for the TextField
                                )
                                IconButton(
                                    onClick = {
                                        inputValuePrice = (inputValuePrice + 1)
                                    },
                                    modifier = Modifier
                                        .width(100.dp)
                                ) {
                                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Button(
                        enabled = inventory.isNotEmpty(),
                        onClick = {
                            val newAd = SellingItemData(
                                item = selectedItem,
                                price = inputValuePrice,
                                quantity = inputValueQuantity)

                            marketViewModel.createNewAd(newAd)

                            // After creating the new ad, navigate back to the market screen
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



@Preview(showBackground = true)
@Composable
fun CreateAdScreenPreview() {
    AppTheme{
        CreateAdScreen(onToMarketScreen = {})
    }
}