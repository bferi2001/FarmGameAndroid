package hu.bme.aut.szoftarch.farmgame.feature.market.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.szoftarch.farmgame.ui.theme.FarmGameAndroidTheme


@Composable
fun SellingItem(title: String, price: Int, count: Int) {
    /*Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 2.dp
    ) {*/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Price: $price")
                Text(text = "Count: $count")
            }

            // Input field with + and - buttons
            var inputValue by remember { mutableIntStateOf(0) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { inputValue = (inputValue - 1).coerceAtLeast(0) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease")
                }
                TextField(
                    value = inputValue.toString(),
                    onValueChange = { newValue ->
                        inputValue = newValue.toIntOrNull() ?: 0
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(70.dp) // Set a specific width for the TextField
                )
                IconButton(
                    onClick = { inputValue = (inputValue + 1).coerceAtMost(count) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase")
                }
            }
        }
    //}
}




@Preview(showBackground = true)
@Composable
fun SellingItemPreview() {
    FarmGameAndroidTheme{
        SellingItem("Item", 1000, 10)
    }
}