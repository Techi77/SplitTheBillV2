package com.stb.registration

import androidx.annotation.StringRes
import com.google.firebase.auth.FirebaseUser
import com.stb.appbase.UiEvent
import com.stb.appbase.UiState
import com.stb.ui.registration.R
import java.lang.Exception

data class RegistrationUiState(
    val user: FirebaseUser? = null,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val switcherState: Switcher = Switcher.LOGIN,
    val buttonEnabled: Boolean = false,
    val passwordRequirements: PasswordConditions = PasswordConditions(
        fourOrMoreDigits = null,
        fourOrMoreLetters = null
    ),
    val showProgress: Boolean = false,
    val isValidEmail: Boolean = true
) : UiState {
    data class PasswordConditions(val fourOrMoreDigits: Boolean?, val fourOrMoreLetters: Boolean?)
}

enum class Switcher(@StringRes val textRes: Int) {
    LOGIN(R.string.login),
    REGISTRATION(R.string.registration)
}

sealed interface RegistrationUiEvent : UiEvent {
    data class CatchError(val error: Exception) : RegistrationUiEvent
    data object GoToMainScreen : RegistrationUiEvent
}