package hu.bme.aut.szoftarch.farmgame.feature.market.createad

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import hu.bme.aut.szoftarch.farmgame.feature.market.MarketViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(
    marketViewModel: MarketViewModel = viewModel(),
    onToMarketScreen: () -> Unit
) {
    var inputValue by remember { mutableIntStateOf(0) }

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Select an option") }
    val options = listOf("Wheat", "Corn", "Carrot")
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
                    .padding(innerPadding)
                    .fillMaxSize(),
                    //.background(woodColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row {
                    // DropDown menu for sellable item selection
                    DropDownDemo()
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

                Button(
                    onClick = {
                        if(inputValue <= 0 && !options.contains(selectedOptionText))
                        {
                            return@Button
                        }

                        onToMarketScreen()
                    }
                ) {
                    Text("Create ad")
                }

            }
        }
    }
}

@Composable
fun DropDownDemo() {

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableIntStateOf(0)
    }

    val usernames = listOf("Alexander", "Isabella", "Benjamin", "Sophia", "Christopher")

    Column(
        //modifier = Modifier.fillMaxSize(),
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
                Text(text = usernames[itemPosition.intValue])
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                usernames.forEachIndexed { index, username ->
                    DropdownMenuItem(text = {
                        Text(text = username)
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