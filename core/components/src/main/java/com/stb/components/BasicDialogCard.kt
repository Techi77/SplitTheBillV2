package com.stb.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stb.theme.ui.Border
import com.stb.theme.ui.BorderDark
import com.stb.theme.ui.SplitTheBillTheme
import com.stb.theme.ui.getColorTheme

@Composable
fun BasicDialogCard(
    title: String? = null,
    subtitle: String? = null,
    icon: ImageBitmap? = ImageBitmap.imageResource(R.drawable.ic_pizza),
    buttonText: String? = null,
    onButtonClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(28.dp)
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .background(
                getColorTheme().primaryContainer,
                shape
            )
            .border(
                width = 1.dp,
                color = if (isSystemInDarkTheme()) BorderDark else Border,
                shape = shape
            )
            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        icon?.let{
            Image(
                bitmap = icon,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
            )
        }
        if (!title.isNullOrBlank())
            Text(
                text = title,
                color = getColorTheme().onPrimaryContainer,
                fontSize = 24.sp
            )
        if (!subtitle.isNullOrBlank())
            Text(
                text = subtitle,
                color = getColorTheme().onPrimaryContainer,
                fontSize = 14.sp
            )
        if (!buttonText.isNullOrBlank())
            RegistrationDialogButton(
                text = buttonText,
                onClick = onButtonClick
            )
    }
}

@Preview
@Composable
private fun BasicCardWithButtonPreview(){
    SplitTheBillTheme{
        BasicDialogCard(
            title = "title",
            subtitle = "subtitle",
            buttonText = "buttonText"
        )
    }
}