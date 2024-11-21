package hu.bme.aut.szoftarch.farmgame.feature.login


import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onToMap: () -> Unit,
) {
    //https://www.youtube.com/watch?v=P_jZMDmodG4

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = loginViewModel.tokenState) {
        loginViewModel.tokenState.collect {
            when (it) {
                is LoginViewModel.TokenState.Idle -> {}
                is LoginViewModel.TokenState.Loading -> {}
                is LoginViewModel.TokenState.Success -> {
                    val googleIdToken = it.token
                    Log.i(TAG, "GoogleToken: "+googleIdToken)
                    onToMap()
                }
                is LoginViewModel.TokenState.Error -> {
                    snackbarHostState.showSnackbar(it.message, duration = SnackbarDuration.Short)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Column {
                        Text(
                            text = "Farm Game",
                        )
                        Text(
                            text = "Login Screen",
                        )
                    }
                }
            )
        },
        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val context= LocalContext.current
            Button(
                onClick = {
                    loginViewModel.getToken(context)
                }
            ) {
                Text(text = "Login with Google account")
            }
            Button(
                onClick = onToMap
            ) {
                Text(text = "open map without login (debug)")
            }
        }
    }

}