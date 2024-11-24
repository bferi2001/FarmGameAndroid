package hu.bme.aut.szoftarch.farmgame.api

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import hu.bme.aut.szoftarch.farmgame.R
import java.security.MessageDigest
import java.util.UUID

object LoginHandler {
    var googleIdTokenCredential: GoogleIdTokenCredential? = null
    var token: String? = googleIdTokenCredential?.idToken
    suspend fun fetchToken(context: Context): GoogleIdTokenCredential? {
        try{
            val credentialManager = CredentialManager.create(context)

            val rawNonce = UUID.randomUUID().toString()
            val bytes = rawNonce.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

            val clientId = context.getString(R.string.googleServiceToken)
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(clientId)
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

            googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)

            token = googleIdTokenCredential?.idToken
            return googleIdTokenCredential
        }
        catch (e: Exception){
            throw e
        }
    }
}