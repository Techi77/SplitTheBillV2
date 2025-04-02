package com.stb.registration

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.stb.appbase.BaseViewModel
import com.stb.preferences.DataStoreManager
import com.stb.preferences.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val regAndAuthUseCase: RegistrationAndAuthorizationUseCase,
    application: Application
) : BaseViewModel<RegistrationUiState, RegistrationUiEvent>() {
    override fun createInitialState(): RegistrationUiState = RegistrationUiState()

    private fun checkButtonEnabled() {
        updateState {
            copy(
                buttonEnabled = emailCheck() && passwordCheck() && confirmPasswordCheck()
            )
        }
    }

    private fun emailCheck() =
        with(state.value) {
            email.isNotBlank() && isValidEmail
        }

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
                email = email,
                isValidEmail = email.isValidEmail()
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

    private fun String.isValidEmail(): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return matches(emailRegex.toRegex())
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

    private val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(application.applicationContext)
    }

    fun loginOrRegisterByEmailAndPassword() {
        updateState {
            copy(
                showProgress = true
            )
        }
        val state = state.value
        viewModelScope.launch {
            try {
                val result = if (state.switcherState == Switcher.REGISTRATION) {
                    regAndAuthUseCase.registerByEmailAndPassword(
                        email = state.email,
                        password = state.password
                    )
                } else regAndAuthUseCase.loginByEmailAndPassword(
                    email = state.email,
                    password = state.password
                )
                updateState {
                    copy(
                        showProgress = false,
                    )
                }
                dataStoreManager.setAuthorizedUser(
                    UserData(
                        uid = result.user?.uid.orEmpty(),
                        email = result.user?.email,
                        displayName = result.user?.displayName.orEmpty(),
                        isEmailVerified = result.user?.isEmailVerified ?: true,
                    )
                )
                pushEvent(RegistrationUiEvent.GoToMainScreen)
            } catch (e: Exception) {
                updateState {
                    copy(
                        showProgress = false
                    )
                }
                pushEvent(RegistrationUiEvent.CatchError(e))
            }
        }
    }

    fun signInWithGoogle() {
        updateState {
            copy(
                showProgress = true
            )
        }
        viewModelScope.launch {
            val result = regAndAuthUseCase.signInWithGoogle()
            if (result.isSuccess) {
                updateState {
                    copy(
                        showProgress = false,
                    )
                }
                val user = result.getOrNull()
                dataStoreManager.setAuthorizedUser(
                    UserData(
                        uid = user?.uid.orEmpty(),
                        email = user?.email,
                        displayName = user?.displayName.orEmpty(),
                        isEmailVerified = user?.isEmailVerified ?: true,
                    )
                )
                pushEvent(RegistrationUiEvent.GoToMainScreen)
            } else if (result.isFailure) {
                updateState {
                    copy(
                        showProgress = false
                    )
                }
                pushEvent(RegistrationUiEvent.CatchError(Exception(result.exceptionOrNull()?.message)))
            }
        }
    }
}