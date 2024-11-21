package hu.bme.aut.szoftarch.farmgame.feature.market


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.szoftarch.farmgame.data.market.SellingItemData
import hu.bme.aut.szoftarch.farmgame.feature.market.items.AdItem
import hu.bme.aut.szoftarch.farmgame.feature.market.items.SellingItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    onToMap: () -> Unit,
    onToCreateAd: () -> Unit,
    viewModel: MarketViewModel = viewModel(),
) {
    val context = LocalContext.current
    var totalPrice by remember { mutableIntStateOf(0) } // State

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

            Row {
                // Column for ads
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "List of ads",
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onToCreateAd
                        ) {
                            Text("Create ad")
                        }
                    }
                    LazyColumn {
                         items(viewModel.adItems.size) { i ->
                            AdItem(
                                item = viewModel.adItems[i].item,
                                price = viewModel.adItems[i].price,
                                count = viewModel.adItems[i].count,
                                userName = viewModel.adItems[i].seller,
                            ) {
                                Toast.makeText(context, "Buying ad item...", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                Divider(
                    modifier = Modifier
                        .width(1.dp) // Set divider width
                        .fillMaxHeight(), // Make divider fill the height
                    color = MaterialTheme.colorScheme.secondaryContainer // Set divider color
                )

                // Column for selling items
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Quick sell items",
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: $totalPrice")
                        Button(
                            onClick =
                            {
                                viewModel.sellingItems.forEach { item ->
                                    item.quantity -= item.sellCount
                                }
                                /* TODO Handle giving money to user */
                                totalPrice = 0
                                Toast.makeText(context, "Selling item...", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Text("Sell")
                        }
                    }

                    LazyColumn {
                        items(viewModel.sellingItems.size) { i ->
                            SellingItem(
                                item = viewModel.sellingItems[i].item,
                                price = viewModel.sellingItems[i].price,
                                quantity = viewModel.sellingItems[i].quantity,
                                secondary = i % 2 == 0
                            ) { it ->
                                viewModel.sellingItems[i].sellCount = it
                                totalPrice = calcTotalPrice(viewModel.sellingItems)
                            }
                        }
                    }

                }
            }
        }
    }
}

private fun calcTotalPrice(sellingItems: List<SellingItemData>): Int {
    var totalPrice = 0
    sellingItems.forEach  { item ->
        totalPrice += item.price * item.sellCount
    }
    return totalPrice
}