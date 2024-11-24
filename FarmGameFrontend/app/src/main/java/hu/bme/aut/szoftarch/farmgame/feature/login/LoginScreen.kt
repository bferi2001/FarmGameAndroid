package hu.bme.aut.szoftarch.farmgame.feature.login


import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.compose.earthTone
import com.example.compose.woodColor
import com.example.compose.woodLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    message: String? = null,
    loginViewModel: LoginViewModel = viewModel(),
    onToMap: () -> Unit,
) {
    //https://www.youtube.com/watch?v=P_jZMDmodG4

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        }
    }

    LaunchedEffect(key1 = loginViewModel.tokenState) {
        loginViewModel.tokenState.collect {
            when (it) {
                is LoginViewModel.TokenState.Idle -> {}
                is LoginViewModel.TokenState.Loading -> {}
                is LoginViewModel.TokenState.Success -> {
                    val googleIdToken = it.token
                    Log.i(TAG, "GoogleToken: $googleIdToken")
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
                modifier = Modifier.border(4.dp, earthTone),
                colors = topAppBarColors(
                    containerColor = woodLight,
                    titleContentColor = MaterialTheme.colorScheme.primary
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
                .fillMaxSize()
                .background(woodColor),
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
        }
    }

}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppTheme{
        LoginScreen(onToMap = {})
    }
}