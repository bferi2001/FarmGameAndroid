package hu.bme.aut.szoftarch.farmgame.feature.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable


@Composable
fun LoginScreen(
    onToMapDebug: () -> Unit,
    //viewModel: LoginUserViewModel = viewModel(factory = LoginUserViewModel.Factory)
) {
    Column {
        Text(
            text = "Farm Game",
        )
        Text(
            text = "Login Screen",
        )
        Button(
            onClick = onToMapDebug
        ) {
            Text(text = "pályára")
        }
    }

}