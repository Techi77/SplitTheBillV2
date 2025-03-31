package com.stb.registration

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.stb.registration.FirebaseConstants.WEB_CLIENT_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.jvm.Throws

class RegistrationAndAuthorizationUseCase @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    // REGISTRATION AND LOGIN BY EMAIL AND PASSWORD

    suspend fun registerByEmailAndPassword(email: String, password: String): AuthResult =
        auth.createUserWithEmailAndPassword(email, password)
            .await()

    suspend fun loginByEmailAndPassword(email: String, password: String): AuthResult =
        auth.signInWithEmailAndPassword(email, password)
            .await()

    // SIGN IN WITH GOOGLE

    private val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(WEB_CLIENT_ID)
        .setFilterByAuthorizedAccounts(false)
        .build()

    private val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    @Throws
    private suspend fun handleSignIn(credential: Credential): AuthResult {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            return firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            throw Exception("Wrong credential")
        }
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential)
            .await()
    }

    suspend fun signInWithGoogle(): Result<FirebaseUser> {
        val credentialManager = CredentialManager.create(appContext)
        try {
            val credential = credentialManager.getCredential(
                context = appContext,
                request = request
            ).credential
            val result = handleSignIn(credential)
            return result.user?.let { Result.success(it) }
                ?: Result.failure(Exception("No such user"))
        } catch (credentialException: GetCredentialException) {
            return Result.failure(credentialException)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}