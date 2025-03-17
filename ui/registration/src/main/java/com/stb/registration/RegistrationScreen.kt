package com.stb.registration

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stb.theme.RegistrationDialog
import com.stb.theme.ui.getColorTheme
import com.stb.ui.registration.R
import com.stb.theme.R as MainR

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
        RegistrationDialog(
            text = stringResource(R.string.login_or_register),
            body = {
                Text(
                    "разработчики точно запилят другие пути регистрации, а пока у них лапки",
                    color = getColorTheme().onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            },
            buttonText = stringResource(R.string.by_google),
            buttonOnClick = {
                // TODO
            }
        )
    }
}

@Composable
@Preview
private fun RegistrationCardPreview() {
    RegistrationCard()
}