package hu.bme.aut.szoftarch.farmgame.feature.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class LoginViewModel @Inject constructor() : ViewModel() {
    sealed class TokenState {
        object Idle : TokenState()
        object Loading : TokenState()
        data class Success(val token: String) : TokenState()
        data class Error(val message: String) : TokenState()
    }

    private val _tokenState = MutableStateFlow<TokenState>(TokenState.Idle)
    val tokenState = _tokenState.asStateFlow()

    fun getToken(context: Context) {
        _tokenState.value = TokenState.Loading
        viewModelScope.launch {
            try{
                val googleIdToken = LoginHandler.fetchToken(context)
                _tokenState.value = TokenState.Success(googleIdToken.idToken)
            }
            catch (e: Exception) {
                _tokenState.value = TokenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}