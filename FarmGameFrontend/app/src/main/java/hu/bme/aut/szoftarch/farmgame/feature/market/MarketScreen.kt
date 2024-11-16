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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hu.bme.aut.szoftarch.farmgame.feature.market.items.AdItem
import hu.bme.aut.szoftarch.farmgame.feature.market.items.SellingItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    onToMap: () -> Unit,
) {
    val context = LocalContext.current
    var totalPrice by remember { mutableStateOf(0) } // State

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
                    LazyColumn() {
                        item {
                            AdItem("Wheat", 5, 20, "TestUser1")
                            AdItem("Corn", 10, 30, "test_user_2")
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
                        text = "Sell items",
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
                        Button(onClick = { /* TODO Handle sell button click */ }) {
                            Text("Sell")
                        }
                    }
                    LazyColumn() {
                        item {
                            SellingItem("Wheat", 1, 2) {}
                            SellingItem("Corn", 3, 4) {}
                            SellingItem("Carrot", 3000, 30) {}
                        }
                    }
                }
            }

        }
    }
}