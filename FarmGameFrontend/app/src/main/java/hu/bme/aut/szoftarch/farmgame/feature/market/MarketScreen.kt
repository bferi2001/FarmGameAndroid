package hu.bme.aut.szoftarch.farmgame.feature.market


import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.compose.earthTone
import com.example.compose.woodLight
import hu.bme.aut.szoftarch.farmgame.data.market.AdItemData
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
    var adItems = remember { mutableStateListOf<AdItemData>() } // State
    var sellingItems = remember { mutableStateListOf<SellingItemData>() } // State
    LaunchedEffect(key1 = viewModel.loadingState) {
        viewModel.loadingState.collect{
            when(it){
                is MarketViewModel.LoadingState.Loaded -> {
                    adItems.clear()
                    adItems.addAll(it.items)
                    sellingItems.clear()
                    sellingItems.addAll(it.inventory)
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
                modifier = Modifier.border(4.dp, earthTone),
                colors = topAppBarColors(
                    containerColor = woodLight,
                    titleContentColor = MaterialTheme.colorScheme.primary
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
                .clickable(enabled = false, onClick = {})
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
                        items(adItems.size) { i ->
                            AdItem(
                                item = adItems[i].item,
                                price = adItems[i].price,
                                count = adItems[i].count,
                                userName = adItems[i].seller,
                                deadline = adItems[i].deadline!!,
                            ) {
                                viewModel.buyAd(adItems[i].id)
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
                                sellingItems.forEach { item ->
                                    try {
                                        if(item.sellCount > 0) {
                                            item.quantity -= item.sellCount

                                            val itemSell = SellingItemData(
                                                item = item.item,
                                                price = item.price,
                                                quantity = item.sellCount)

                                            viewModel.QuickSell(itemSell)
                                        }

                                    } catch (e: Exception) { }

                                }
                                totalPrice = 0
                                Toast.makeText(context, "Selling item...", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Text("Sell")
                        }
                    }

                    LazyColumn {
                        items(sellingItems.size) { i ->
                            SellingItem(
                                item = sellingItems[i].item,
                                price = sellingItems[i].price,
                                quantity = sellingItems[i].quantity,
                                secondary = i % 2 == 0
                            ) { it ->
                                sellingItems[i].sellCount = it
                                totalPrice = calcTotalPrice(sellingItems)
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



@Preview(showBackground = true)
@Composable
fun MarketScreenPreview() {
    AppTheme{
        MarketScreen({}, {})
    }
}