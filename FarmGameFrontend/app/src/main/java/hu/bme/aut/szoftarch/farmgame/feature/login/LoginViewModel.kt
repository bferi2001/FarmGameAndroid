package hu.bme.aut.szoftarch.farmgame.feature.login

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.szoftarch.farmgame.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {
    private val credentialManager = CredentialManager.create(context)

    private val rawNonce = UUID.randomUUID().toString()
    private val bytes = rawNonce.toByteArray()
    private val md = MessageDigest.getInstance("SHA-256")
    private val digest = md.digest(bytes)
    private val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(context.getString(R.string.googleServiceToken))
        .setNonce(hashedNonce)
        .build()

    private val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    sealed class TokenState {
        object Idle : TokenState()
        object Loading : TokenState()
        data class Success(val token: String) : TokenState()
        data class Error(val message: String) : TokenState()
    }

    private val _tokenState = MutableStateFlow<TokenState>(TokenState.Idle)
    val tokenState = _tokenState.asStateFlow()

    fun getToken() {
        _tokenState.value = TokenState.Loading
        viewModelScope.launch {
            try{
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken
                _tokenState.value = TokenState.Success(googleIdToken)
            }
            catch (e: Exception) {
                _tokenState.value = TokenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}