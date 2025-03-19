package com.stb.registration

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stb.components.RegistrationDialogButton
import com.stb.components.StbTextField
import com.stb.components.SwitcherWithText
import com.stb.theme.ui.Border
import com.stb.theme.ui.BorderDark
import com.stb.theme.ui.getColorTheme
import com.stb.ui.registration.R
import com.stb.components.R as MainR

@Composable
fun RegistrationScreen(
    viewModel: RegistrationScreenViewModel = viewModel()
) {
    val activity = LocalActivity.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold { paddingValues ->
        RegistrationCard(
            paddingValues = paddingValues,
            actionHandler = remember {
                { action ->
                    when (action) {
                        RegistrationAction.LogInByGoogle -> {
                            activity?.let {
                                viewModel.signInWithGoogle(it)
                            }
                        }

                        is RegistrationAction.SwitcherSelect -> {
                            viewModel.setSwitcherState(action.index)
                        }

                        is RegistrationAction.SetEmail ->
                            viewModel.setEmail(action.email)

                        is RegistrationAction.SetPassword ->
                            viewModel.setPassword(action.password)

                        is RegistrationAction.SetPasswordConfirm ->
                            viewModel.setPasswordConfirm(action.passwordConfirm)
                    }
                }
            },
            state = state
        )
    }
}

@Composable
private fun RegistrationCard(
    paddingValues: PaddingValues = PaddingValues(),
    actionHandler: (RegistrationAction) -> Unit = {},
    state: RegistrationUiState
) {
    Box(
        modifier = Modifier
            .paint(
                painter = painterResource(
                    if (isSystemInDarkTheme()) MainR.drawable.bg_gradient_dark
                    else MainR.drawable.bg_gradient_light
                ),
                contentScale = ContentScale.FillBounds
            )
            .fillMaxSize()
            .padding(paddingValues)
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
                    bitmap = ImageBitmap.imageResource(com.stb.components.R.drawable.ic_pizza),
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
                    Text("Или быстрее")
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
            labelText = stringResource(R.string.email)
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
            labelText = stringResource(R.string.password)
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
                labelText = stringResource(R.string.confirm_password)
            )
        }
    }
    item(
        key = "goButton",
        contentType = RegistrationContentType.GO_BUTTON
    ) {
        Button(
            modifier = Modifier
                .animateItem()
                .widthIn(min = 200.dp),
            onClick = {
                //TODO
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = getColorTheme().primary
            )
        ) {
            Text(stringResource(R.string.go))
        }
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
}

@Composable
@Preview
private fun RegistrationCardPreview() {
    RegistrationCard(
        state = RegistrationUiState()
    )
}