package com.stb.registration

import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.stb.appbase.BaseViewModel
import com.stb.registration.FirebaseConstants.WEB_CLIENT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor() :
    BaseViewModel<RegistrationUiState, Nothing>() {
    override fun createInitialState(): RegistrationUiState = RegistrationUiState()

    fun setSwitcherState(index: Int){
        updateState {
            copy(
                switcherState = Switcher.entries[index]
            )
        }
    }

    fun setEmail(email: String){
        updateState {
            copy(
                email = email
            )
        }
    }

    fun setPassword(password: String){
        updateState {
            copy(
                password = password
            )
        }
    }

    fun setPasswordConfirm(password: String){
        updateState {
            copy(
                confirmPassword = password
            )
        }
    }

    private val auth: FirebaseAuth by lazy { Firebase.auth }

    private val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(WEB_CLIENT_ID)
        .setFilterByAuthorizedAccounts(false)
        .build()

    private val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    private fun handleSignIn(credential: Credential, activity: Activity) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken, activity = activity)
        } else {
            updateState {
                copy(error = Exception("Wrong credential"))
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, activity: Activity) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    updateState {
                        copy(
                            user = auth.currentUser
                        )
                    }
                } else {
                    updateState {
                        copy(
                            error = task.exception
                        )
                    }
                }
            }
    }

    fun signInWithGoogle(activity: Activity) {
        val credentialManager = CredentialManager.create(activity)
        credentialManager.getCredentialAsync(
            request = request,
            context = activity,
            cancellationSignal = null,
            executor = ContextCompat.getMainExecutor(activity),
            callback = object :
                CredentialManagerCallback<GetCredentialResponse, GetCredentialException> {
                override fun onResult(result: GetCredentialResponse) {
                    val credential = result.credential
                    handleSignIn(credential, activity)
                }

                override fun onError(e: GetCredentialException) {
                    updateState {
                        copy(
                            error = e
                        )
                    }
                }
            }
        )
    }
}