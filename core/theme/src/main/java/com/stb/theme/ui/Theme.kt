package com.stb.theme.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

private val LightColorScheme = lightColorScheme(
    //Основной цвет темы, который используется для ключевых элементов интерфейса, таких как кнопки, заголовки, активные элементы.
    //Пример: Цвет фона кнопки, акцентные элементы.
    primary = MainPurple,
    //Цвет, который контрастирует с primary. Используется для текста или иконок, размещенных на фоне primary.
    //Пример: Текст на кнопке с цветом primary.
    onPrimary = White,
    //Цвет контейнера для элементов, связанных с primary. Обычно используется для менее важных элементов, чем primary.
    //Пример: Фон карточки, связанной с основным действием.
    primaryContainer = BackgroundWhite,
    //Цвет, который контрастирует с primaryContainer. Используется для текста или иконок на фоне primaryContainer.
    //Пример: Текст на карточке с фоном primaryContainer.
    onPrimaryContainer = Black_Transparency80,
    //Вторичный цвет темы, который используется для менее важных элементов, чем primary.
    //Пример: Второстепенные кнопки, иконки.
    secondary = MainBlue,
    //Цвет, который контрастирует с secondary. Используется для текста или иконок на фоне secondary.
    //Пример: Текст на кнопке с цветом secondary.
    onSecondary = White,
    //Цвет для границ элементов.
    //Пример: Граница текстового поля.
    outline = BorderBlack,
    //Цвет для отображения ошибок.
    //Пример: Текст ошибки, иконка ошибки.
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = MainPurple_Transparency80,
    onPrimary = White_Transparency80,
    primaryContainer = BackgroundBlack,
    onPrimaryContainer = White_Transparency80,
    secondaryContainer = MainBlueDark,
    onSecondary = White_Transparency80,
    outline = BorderWhite,
    error = ErrorRedDark
)

@Composable
fun SplitTheBillTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = getColorTheme(),
        typography = Typography,
        content = content
    )
}

@Composable
@ReadOnlyComposable
fun getColorTheme(inverse: Boolean = false) =
    if ((!inverse && isSystemInDarkTheme()) || (inverse && !isSystemInDarkTheme())) DarkColorScheme
    else LightColorScheme

@Composable
fun getBaseOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = getColorTheme().onPrimaryContainer,
    focusedBorderColor = getColorTheme().secondary,
    focusedLabelColor = getColorTheme().secondary,
    unfocusedBorderColor = getColorTheme().onPrimaryContainer,
    unfocusedLabelColor = getColorTheme().onPrimaryContainer,
    errorTextColor = getColorTheme().onPrimaryContainer,
    errorPlaceholderColor = getColorTheme().error,
    errorLabelColor = getColorTheme().error,
    errorSupportingTextColor = getColorTheme().error,
    // TrailingIcon
    errorTrailingIconColor = getColorTheme().onPrimaryContainer,
    focusedTrailingIconColor = getColorTheme().onPrimaryContainer,
    unfocusedTrailingIconColor = getColorTheme().onPrimaryContainer,
)