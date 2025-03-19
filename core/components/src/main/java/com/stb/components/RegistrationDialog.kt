package com.stb.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stb.theme.ui.Border
import com.stb.theme.ui.BorderDark
import com.stb.theme.ui.getColorTheme

@Composable
fun RegistrationDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    body: (@Composable () -> Unit)? = null,
    buttons:  @Composable() (ColumnScope.() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(28.dp)
    Card(
        modifier = modifier.animateContentSize(),
        colors = CardDefaults.cardColors().copy(
            containerColor = getColorTheme().primaryContainer
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSystemInDarkTheme()) BorderDark else Border,
        ),
        shape = shape
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.ic_pizza),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
            title?.let {
                Text(
                    text = it,
                    color = getColorTheme().onPrimaryContainer,
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center

                )
            }
            body?.let {
                it()
            }
            buttons?.let{
                it()
            }
        }
    }
}

@Composable
@Preview
private fun RegistrationDialogPreview() {
    RegistrationDialog(
        title = "Войти",
        body = {
            Text(
                "разработчики точно запилят другие пути регистрации, а пока у них лапки",
                color = getColorTheme().onPrimaryContainer,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        },
        buttons = {
            RegistrationDialogButton(
                text = "С помощью\nGoogle",
                onClick = {}
            )
        }
    )
}