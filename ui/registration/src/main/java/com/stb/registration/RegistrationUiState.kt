package com.stb.registration

import com.google.firebase.auth.FirebaseUser
import com.stb.appbase.UiState
import java.lang.Exception

data class RegistrationUiState(
    val user: FirebaseUser? = null,
    val error: Exception? = null
) : UiState