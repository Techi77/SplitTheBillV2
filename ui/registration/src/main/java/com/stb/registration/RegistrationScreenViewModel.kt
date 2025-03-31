package com.stb.registration

import androidx.lifecycle.viewModelScope
import com.stb.appbase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val regAndAuthUseCase: RegistrationAndAuthorizationUseCase
) : BaseViewModel<RegistrationUiState, RegistrationUiEvent>() {
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
                        user = result.user
                    )
                }
            } catch (e: Exception) {
                updateState {
                    copy(
                        showProgress = false,
                        error = e
                    )
                }
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
                        user = result.getOrNull()
                    )
                }
            } else if (result.isFailure) {
                updateState {
                    copy(
                        showProgress = false,
                        error = result.exceptionOrNull() as? Exception
                    )
                }
            }
        }
    }
}