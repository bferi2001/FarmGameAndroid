package hu.bme.aut.szoftarch.farmgame.api

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import hu.bme.aut.szoftarch.farmgame.R
import java.security.MessageDigest
import java.util.UUID

interface LoginHandler{
    suspend fun fetchToken(context: Context): GoogleIdTokenCredential
}

class LoginHandlerImpl : LoginHandler {
    override suspend fun fetchToken(context: Context): GoogleIdTokenCredential {
        try{
            val credentialManager = CredentialManager.create(context)

            val rawNonce = UUID.randomUUID().toString()
            val bytes = rawNonce.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.googleServiceToken))
                .setNonce(hashedNonce)
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )
            val credential = result.credential

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)

            return googleIdTokenCredential
        }
        catch (e: Exception){
            throw e
        }
    }
}