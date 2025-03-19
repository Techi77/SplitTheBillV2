package com.stb.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stb.theme.ui.getColorTheme

@Composable
fun RegistrationDialogButton(
    text: String,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(
            text = text,
            color = getColorTheme().secondary,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            lineHeight = 28.sp,
            modifier = Modifier
                .padding(
                    vertical = 16.dp,
                    horizontal = 44.dp
                )
        )
    }
}

@Composable
@Preview
private fun RegistrationDialogButtonPreview() {
    RegistrationDialogButton(
        text = "С помощью\nGoogle",
        onClick = {}
    )
}