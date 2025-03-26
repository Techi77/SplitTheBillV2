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
    BaseViewModel<RegistrationUiState, RegistrationUiEvent>() {
    override fun createInitialState(): RegistrationUiState = RegistrationUiState()

    private fun checkButtonEnabled() {
        updateState {
            copy(
                buttonEnabled = emailCheck() && passwordCheck() && confirmPasswordCheck()
            )
        }
    }

    private fun emailCheck() = state.value.email.isNotBlank()
    private fun passwordCheck() = state.value.password.isNotBlank() &&
            (state.value.passwordRequirements == RegistrationUiState.PasswordConditions(
                fourOrMoreLetters = true,
                fourOrMoreDigits = true
            ) || state.value.switcherState == Switcher.LOGIN)

    private fun confirmPasswordCheck() = with(state.value) {
        (confirmPassword == password)
                || switcherState == Switcher.LOGIN
    }

    fun setSwitcherState(index: Int) {
        updateState {
            copy(
                switcherState = Switcher.entries[index]
            )
        }
        checkButtonEnabled()
    }

    fun setEmail(email: String) {
        updateState {
            copy(
                email = email
            )
        }
        checkButtonEnabled()
    }

    fun setPassword(password: String) {
        updateState {
            copy(
                password = password,
                passwordRequirements = RegistrationUiState.PasswordConditions(
                    fourOrMoreDigits = password.hasFourOrMoreDigits(),
                    fourOrMoreLetters = password.hasFourOrMoreLetters(),
                )
            )
        }
        checkButtonEnabled()
    }

    private fun String.hasFourOrMoreDigits(): Boolean {
        val regex = Regex("\\d{4,}")
        return regex.containsMatchIn(this)
    }

    private fun String.hasFourOrMoreLetters(): Boolean {
        val regex = Regex("[a-zA-Zа-яА-Я]{4,}")
        return regex.containsMatchIn(this)
    }

    fun setPasswordConfirm(password: String) {
        updateState {
            copy(
                confirmPassword = password
            )
        }
        checkButtonEnabled()
    }

    private val auth: FirebaseAuth by lazy { Firebase.auth }

    fun loginOrRegisterByEmailAndPassword(activity: Activity) {
        updateState {
            copy(
                showProgress = true
            )
        }
        if (state.value.switcherState == Switcher.REGISTRATION) {
            registerByEmailAndPassword(activity)
        } else loginByEmailAndPassword(activity)
    }

    // REGISTER BY EMAIL+PASSWORD

    private fun registerByEmailAndPassword(activity: Activity) {
        println("Techi: registerByEmailAndPassword")
        auth.createUserWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener(activity) { task ->
                println("Techi: registerByEmailAndPassword addOnCompleteListener task=$task")
                if (task.isSuccessful) {
                    updateState {
                        copy(
                            user = auth.currentUser
                        )
                    }
                } else {
                    task.exception?.let { pushEvent(RegistrationUiEvent.CatchError(it)) }
                }
                updateState {
                    copy(
                        showProgress = false
                    )
                }
            }
    }

    // LOGIN BY EMAIL+PASSWORD

    private fun loginByEmailAndPassword(activity: Activity) {
        println("Techi: loginByEmailAndPassword")
        auth.signInWithEmailAndPassword(state.value.email, state.value.password)
            .addOnCompleteListener(activity) { task ->
                println("Techi: loginByEmailAndPassword addOnCompleteListener task=$task")
                if (task.isSuccessful) {
                    println("Techi: loginByEmailAndPassword task is successful")
                    updateState {
                        copy(
                            user = auth.currentUser
                        )
                    }
                } else {
                    task.exception?.let { pushEvent(RegistrationUiEvent.CatchError(it)) }
                }
                updateState {
                    copy(
                        showProgress = false
                    )
                }
            }
    }

    // LOGIN BY GOOGLE

    private val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(WEB_CLIENT_ID)
        .setFilterByAuthorizedAccounts(false)
        .build()

    private val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    private fun handleSignIn(credential: Credential, activity: Activity) {
        updateState {
            copy(
                showProgress = true
            )
        }
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken, activity = activity)
            updateState {
                copy(
                    showProgress = false
                )
            }
        } else {
            updateState {
                copy(
                    showProgress = false
                )
            }
            pushEvent(RegistrationUiEvent.CatchError(Exception("Wrong credential")))
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
                    task.exception?.let { pushEvent(RegistrationUiEvent.CatchError(it)) }
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
                    pushEvent(RegistrationUiEvent.CatchError(e))
                }
            }
        )
    }
}