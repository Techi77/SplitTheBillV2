package com.stb.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.stb.theme.ui.BackgroundWhite
import com.stb.theme.ui.getBaseOutlinedTextFieldColors

@Composable
fun StbTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    errorText: String? = null,
    singleLine: Boolean = true,
    isPassword: Boolean = false
) {
    var passwordVisible by rememberSaveable { mutableStateOf(!isPassword) }
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(labelText)
        },
        colors = getBaseOutlinedTextFieldColors(),
        isError = !errorText.isNullOrBlank(),
        supportingText = errorText?.let { { Text(it) } },
        singleLine = singleLine,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.ic_eye_opened
                            else R.drawable.ic_eye_closed
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        } else null
    )
}

@Composable
@Preview(backgroundColor = 0xFFFDFDFD)
private fun StbTextFieldPreview() {
    Column(
        modifier = Modifier.background(BackgroundWhite),
    ) {
        StbTextField(
            value = "Hi",
            onValueChange = {},
            labelText = "Unfocused"
        )
        StbTextField(
            value = "Hi",
            onValueChange = {},
            labelText = "Error",
            errorText = "Some error text"
        )
        StbTextField(
            value = "Hi",
            onValueChange = {},
            labelText = "Password",
            isPassword = true
        )
    }
}