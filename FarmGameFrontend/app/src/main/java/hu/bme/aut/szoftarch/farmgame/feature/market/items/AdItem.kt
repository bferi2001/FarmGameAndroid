package hu.bme.aut.szoftarch.farmgame.feature.market.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AdItem(item: String, count: Int, price: Int, userName: String, deadline: LocalDateTime,
           secondary: Boolean = false,
           onBuyClick: () -> Unit) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = item, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Count: $count")
                    Text(text = "Seller: $userName")
                }
                Text(text = "Price: $price")
                Button(
                    onClick = {
                        onBuyClick()
                    }
                ) {
                    Text(text = "Buy")
                }
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            Text(text = "Deadline: ${deadline.format(formatter)}")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AdItemPreview() {
    AppTheme{
        AdItem("Item", 10, 1000, "User", LocalDateTime.now()) {}
    }
}