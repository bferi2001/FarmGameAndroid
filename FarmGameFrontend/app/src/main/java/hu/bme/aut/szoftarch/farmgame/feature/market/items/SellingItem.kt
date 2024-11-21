package hu.bme.aut.szoftarch.farmgame.feature.market.items

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme


@Composable
fun SellingItem(item: String, price: Int, quantity: Int,
                secondary: Boolean = false,
                onInputValueChanged: (Int) -> Unit){
    var inputValue by remember { mutableIntStateOf(0) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (!secondary) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = item, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(1.dp))
                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .width(50.dp),
                    color = MaterialTheme.colorScheme.inversePrimary // Set divider color
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Price: $price")
                Text(text = "Count: $quantity")
            }

            // Input field with + and - buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        inputValue = (inputValue - 1).coerceAtLeast(0)
                        onInputValueChanged(inputValue)
                        },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease")
                }
                TextField(
                    value = inputValue.toString(),
                    onValueChange = { newValue ->
                        inputValue = newValue.toIntOrNull()?.coerceIn(0, quantity) ?: 0
                        onInputValueChanged(inputValue)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(70.dp), // Set a specific width for the TextField
                )
                IconButton(
                    onClick = {
                        inputValue = (inputValue + 1).coerceAtMost(quantity)
                        onInputValueChanged(inputValue)
                        },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase")
                }
            }
        }
}




@Preview(showBackground = true)
@Composable
fun SellingItemPreview() {
    AppTheme {
        SellingItem("Item", 1000, 10) {}
    }
}