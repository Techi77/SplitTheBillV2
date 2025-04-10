package com.stb.registration

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stb.appbase.CollectAsEventWithLifecycle
import com.stb.components.RegistrationDialogButton
import com.stb.components.StbTextField
import com.stb.components.SwitcherWithText
import com.stb.registration.RegistrationErrors.EMAIL_ALREADY_EXISTS
import com.stb.registration.RegistrationErrors.NO_SUCH_EMAIL_ERROR
import com.stb.theme.ui.Border
import com.stb.theme.ui.BorderDark
import com.stb.theme.ui.Green
import com.stb.theme.ui.LightGreen
import com.stb.theme.ui.getColorTheme
import com.stb.ui.registration.R
import com.stb.components.R as MainR

@Composable
fun RegistrationScreen(
    onRegistrationComplete: () -> Unit = {},
    viewModel: RegistrationScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.events.CollectAsEventWithLifecycle {
        when (it) {
            is RegistrationUiEvent.CatchError -> {
                val errorMessage = when {
                    it.error.message.isNullOrBlank() -> stringResource(MainR.string.standard_error)
                    it.error.message!!.startsWith(NO_SUCH_EMAIL_ERROR) -> stringResource(R.string.no_user)
                    it.error.message!!.startsWith(EMAIL_ALREADY_EXISTS) -> stringResource(R.string.email_already_exists)
                    else -> it.error.message!!
                }
                println("Techi: errorMessage=$errorMessage")
                showToast(errorMessage)
            }

            RegistrationUiEvent.GoToMainScreen -> onRegistrationComplete()
        }
    }

    Scaffold(
        contentColor = getColorTheme().primaryContainer
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            RegistrationCard(
                actionHandler = remember {
                    { action ->
                        when (action) {
                            RegistrationAction.LogInByGoogle ->
                                viewModel.signInWithGoogle()

                            is RegistrationAction.SwitcherSelect ->
                                viewModel.setSwitcherState(action.index)

                            is RegistrationAction.SetEmail ->
                                viewModel.setEmail(action.email)

                            is RegistrationAction.SetPassword ->
                                viewModel.setPassword(action.password)

                            is RegistrationAction.SetPasswordConfirm ->
                                viewModel.setPasswordConfirm(action.passwordConfirm)

                            RegistrationAction.GoButtonClick ->
                                viewModel.loginOrRegisterByEmailAndPassword()
                        }
                    }
                },
                state = state
            )
        }
    }
}

@Composable
private fun RegistrationCard(
    actionHandler: (RegistrationAction) -> Unit = {},
    state: RegistrationUiState
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        val shape = RoundedCornerShape(28.dp)
        LazyColumn(
            modifier = Modifier
                .background(
                    getColorTheme().primaryContainer,
                    shape
                )
                .border(
                    width = 1.dp,
                    color = if (isSystemInDarkTheme()) BorderDark else Border,
                    shape = shape
                )
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "Pizza") {
                Image(
                    bitmap = ImageBitmap.imageResource(MainR.drawable.ic_pizza),
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .animateItem()
                )
            }
            dialogBody(
                actionHandler = actionHandler,
                state = state
            )
            item(
                key = "orFaster",
                contentType = RegistrationContentType.DIVIDER
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.animateItem()
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(text = "Или быстрее", color = getColorTheme().onPrimaryContainer)
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }
            }
            item(key = "regButton") {
                RegistrationDialogButton(
                    modifier = Modifier.animateItem(),
                    text = stringResource(R.string.by_google),
                    onClick = {
                        actionHandler(RegistrationAction.LogInByGoogle)
                    }
                )
            }
        }
        val alpha by animateFloatAsState(
            targetValue = if (state.showProgress) 1f else 0f,
            animationSpec = tween(durationMillis = 300),
            label = "progressAlphaAnimation"
        )
        if (alpha > 0f)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { this.alpha = alpha }
                    .background(
                        getColorTheme().primaryContainer,
                        shape
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                )
            }
    }
}

private fun LazyListScope.dialogBody(
    actionHandler: (RegistrationAction) -> Unit = {},
    state: RegistrationUiState
) {
    item(
        key = RegistrationContentType.SWITCHER,
        contentType = RegistrationContentType.SWITCHER,
    ) {
        SwitcherWithText(
            modifier = Modifier.animateItem(),
            items = Switcher.entries.map {
                stringResource(it.textRes)
            },
            onSelectionChange = {
                actionHandler(RegistrationAction.SwitcherSelect(it))
            }
        )
    }
    item(
        key = "login",
        contentType = RegistrationContentType.TEXT_FIELD
    ) {
        StbTextField(
            modifier = Modifier
                .animateItem()
                .fillMaxWidth(),
            value = state.email,
            onValueChange = {
                actionHandler(
                    RegistrationAction.SetEmail(
                        it
                    )
                )
            },
            labelText = stringResource(R.string.email),
            errorText = if (!state.isValidEmail)
                stringResource(R.string.email_is_not_valid)
            else null
        )
    }
    item(
        key = "password",
        contentType = RegistrationContentType.TEXT_FIELD
    ) {
        StbTextField(
            modifier = Modifier
                .animateItem()
                .fillMaxWidth(),
            value = state.password,
            onValueChange = {
                actionHandler(
                    RegistrationAction.SetPassword(
                        it
                    )
                )
            },
            labelText = stringResource(R.string.password),
            isPassword = true,
            infoText = if (state.switcherState == Switcher.REGISTRATION) {
                { BasicText(text = passwordInfo(state)) }
            } else null
        )
    }
    if (state.switcherState == Switcher.REGISTRATION) {
        item(
            key = "confirmPassword",
            contentType = RegistrationContentType.TEXT_FIELD
        ) {
            StbTextField(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth(),
                value = state.confirmPassword,
                onValueChange = {
                    actionHandler(
                        RegistrationAction.SetPasswordConfirm(
                            it
                        )
                    )
                },
                labelText = stringResource(R.string.confirm_password),
                isPassword = true,
                errorText =
                    if (state.confirmPassword.isNotBlank() && state.confirmPassword != state.password)
                        stringResource(R.string.passwords_do_not_match)
                    else null
            )
        }
    }
    item(
        key = "goButton",
        contentType = RegistrationContentType.GO_BUTTON
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Button(
            modifier = Modifier
                .animateItem()
                .widthIn(min = 200.dp),
            onClick = {
                keyboardController?.hide()
                actionHandler(RegistrationAction.GoButtonClick)
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = getColorTheme().primary,
                disabledContainerColor = getColorTheme(inverse = true).primaryContainer.copy(alpha = 0.1f),
                disabledContentColor = getColorTheme().onPrimaryContainer.copy(alpha = 0.5f)
            ),
            enabled = state.buttonEnabled
        ) {
            Text(stringResource(R.string.go))
        }
    }
}

@Composable
private fun passwordInfo(state: RegistrationUiState) = buildAnnotatedString {
    val fourOrMoreLetters = state.passwordRequirements.fourOrMoreLetters
    withStyle(
        style = SpanStyle(
            color =
                if (fourOrMoreLetters == null) getColorTheme().onPrimaryContainer
                else if (fourOrMoreLetters && isSystemInDarkTheme()) LightGreen
                else if (fourOrMoreLetters) Green
                else getColorTheme().error
        )
    ) {
        append(stringResource(R.string.four_letters))
    }
    withStyle(
        SpanStyle(
            color = getColorTheme().onPrimaryContainer
        )
    ) {
        append(" + ")
    }
    val fourOrMoreDigits = state.passwordRequirements.fourOrMoreDigits
    withStyle(
        style = SpanStyle(
            color =
                if (fourOrMoreDigits == null) getColorTheme().onPrimaryContainer
                else if (fourOrMoreDigits && isSystemInDarkTheme()) LightGreen
                else if (fourOrMoreDigits) Green
                else getColorTheme().error
        )
    ) {
        append(stringResource(R.string.four_numbers))
    }
    withStyle(
        SpanStyle(
            color = getColorTheme().onPrimaryContainer
        )
    ) {
        append(stringResource(R.string.password_requirements))
    }
}

private enum class RegistrationContentType {
    SWITCHER, TEXT_FIELD, GO_BUTTON, DIVIDER
}

@Immutable
private sealed interface RegistrationAction {
    data object LogInByGoogle : RegistrationAction
    data class SwitcherSelect(val index: Int) : RegistrationAction
    data class SetEmail(val email: String) : RegistrationAction
    data class SetPassword(val password: String) : RegistrationAction
    data class SetPasswordConfirm(val passwordConfirm: String) : RegistrationAction
    data object GoButtonClick : RegistrationAction
}

@Composable
@Preview
private fun RegistrationCardPreview() {
    RegistrationCard(
        state = RegistrationUiState(
            passwordRequirements = RegistrationUiState.PasswordConditions(
                fourOrMoreDigits = true,
                fourOrMoreLetters = false
            )
        )
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun RegistrationCardDarkThemePreview() {
    RegistrationCard(
        state = RegistrationUiState(
            passwordRequirements = RegistrationUiState.PasswordConditions(
                fourOrMoreDigits = true,
                fourOrMoreLetters = false
            ),
            showProgress = true
        )
    )
}

