package com.stb.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stb.ui.registration.R

@Composable
fun RegistrationScreen() {
    Scaffold { paddingValues ->
        RegistrationCard(paddingValues)
    }
}

@Composable
fun RegistrationCard(
    paddingValues: PaddingValues = PaddingValues()
) {
    Card(
        modifier = Modifier.padding(paddingValues)
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.ic_pizza),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
            Text(text = stringResource(R.string.login_or_register))
        }
    }
}

@Composable
@Preview
private fun RegistrationCardPreview() {
    RegistrationCard()
}