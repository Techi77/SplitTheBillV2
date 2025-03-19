package com.stb.registration

import androidx.annotation.StringRes
import com.google.firebase.auth.FirebaseUser
import com.stb.appbase.UiEvent
import com.stb.appbase.UiState
import com.stb.ui.registration.R
import java.lang.Exception

data class RegistrationUiState(
    val user: FirebaseUser? = null,
    val error: Exception? = null,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val switcherState: Switcher = Switcher.LOGIN,
    val buttonEnabled: Boolean = false
) : UiState

enum class Switcher(@StringRes val textRes: Int) {
    LOGIN(R.string.login),
    REGISTRATION(R.string.registration)
}

sealed interface RegistrationUiEvent : UiEvent {
    data class CatchError(val error: Exception) : RegistrationUiEvent
}