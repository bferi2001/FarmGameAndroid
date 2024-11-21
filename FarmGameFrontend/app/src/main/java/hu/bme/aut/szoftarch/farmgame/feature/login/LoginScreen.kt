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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.compose.earthTone
import com.example.compose.woodColor
import com.example.compose.woodLight
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import hu.bme.aut.szoftarch.farmgame.R
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID


@SuppressLint("ShowToast")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
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
            Button(
                onClick = onToMap
            ) {
                Text(text = "open map without login (debug)")
            }
        }
    }

}