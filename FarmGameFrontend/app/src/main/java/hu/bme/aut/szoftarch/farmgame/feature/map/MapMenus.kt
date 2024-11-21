package hu.bme.aut.szoftarch.farmgame.feature.map

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.compose.earthTone
import com.example.compose.woodLight
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation

@Composable
fun SideMenuScreen(viewModel: MapViewModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val menuOpen by remember { derivedStateOf { viewModel.menuOpen } }

    val menuOffsetX by animateDpAsState(
        targetValue = if (menuOpen != MenuLocation.SIDE) -(screenWidth / 2) else 0.dp
    )

    val menuTitle = viewModel.getSelectedLand()?.getName() ?: "Side menu"

    Box(
        modifier = Modifier
            .offset(menuOffsetX)
            .fillMaxHeight()
            .background(woodLight)
            .width(screenWidth / 2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.weight(1f),
                    text = menuTitle
                )
                Button(
                    onClick = { viewModel.closeMenu() },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Close")
                }
            }
            CreateInteractButtons(viewModel)
        }

    }
}


@Composable
fun BottomMenuContent(viewModel: MapViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            CreateInteractButtons(viewModel)
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { viewModel.closeMenu() },
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Close")
        }

    }
}