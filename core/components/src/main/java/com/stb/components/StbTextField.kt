package com.stb.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stb.theme.ui.BackgroundWhite
import com.stb.theme.ui.getBaseOutlinedTextFieldColors

@Composable
fun StbTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    errorText: String? = null
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(labelText)
        },
        colors = getBaseOutlinedTextFieldColors(),
        isError = !errorText.isNullOrBlank(),
        supportingText = errorText?.let{ { Text(it) } }
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
    }
}