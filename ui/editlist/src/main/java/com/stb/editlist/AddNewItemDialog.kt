package com.stb.editlist

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stb.editlist.entity.ListItem
import com.stb.theme.ui.Green
import com.stb.theme.ui.LightGreen
import com.stb.theme.ui.SplitTheBillTheme
import com.stb.theme.ui.getColorTheme
import com.stb.ui.editlist.R
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddNewItemDialog(
    addNewItem: (ListItem) -> Unit = {},
    hideDialog: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = hideDialog,
        sheetState = sheetState,
        modifier = Modifier
            .widthIn(max = 348.dp)
            .padding(bottom = 23.dp)
    ) {
        AddNewItemDialogBody(
            addNewItem = addNewItem,
            hideDialog = hideDialog,
            sheetState = sheetState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNewItemDialogBody(
    addNewItem: (ListItem) -> Unit = {},
    hideDialog: () -> Unit = {},
    sheetState: SheetState? = null
) {
    val scope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var count by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val checkedState = remember { mutableStateOf(false) }
    LaunchedEffect(checkedState.value) {
        sheetState?.expand()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.name)) },
            isError = showError && title.isBlank()
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = price,
            onValueChange = { price = it.addNumberMask(2) },
            label = { Text(stringResource(R.string.price)) },
            isError = showError && price == "",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            visualTransformation = DecimalMaskTransformation()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it },
                    modifier = Modifier.padding(0.dp),
                )
            }
            Text(
                text = stringResource(R.string.add_quantity)
            )
        }
        if (checkedState.value) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = count,
                onValueChange = { count = it.addNumberMask(3) },
                label = { Text(stringResource(R.string.quantity)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )

            HorizontalDivider(color = getColorTheme().outline)

            val totalString = stringResource(R.string.total)
            val totalCount =
                (price.toDoubleOrNull() ?: 0.0) * (count.toDoubleOrNull() ?: 0.0)
            val totalCountString = totalCount.formatNumberWithLocale()
            Text(
                text = "$totalString: $totalCountString",
                fontSize = 20.sp
            )
        }
        Button(
            onClick = {
                scope.launch { sheetState?.hide() }.invokeOnCompletion {
                    if (sheetState?.isVisible == false) {
                        val resultPrice = price.toDoubleOrNull()
                        if (resultPrice == null || title.isBlank())
                            showError = true
                        else {
                            hideDialog()
                            addNewItem(
                                ListItem(
                                    title = title,
                                    pricePerUnit = resultPrice,
                                    quantityOrWeight = count.toDoubleOrNull() ?: 1.00,
                                )
                            )
                        }
                    }
                }
            },
            colors = ButtonColors(
                containerColor = if (isSystemInDarkTheme()) LightGreen
                else Green,
                contentColor = getColorTheme().onPrimary,
                disabledContainerColor = getColorTheme().onPrimaryContainer.copy(alpha = 0.25F),
                disabledContentColor = getColorTheme().onPrimary
            )
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(0.dp),
                    contentDescription = "Check"
                )
                Text(
                    text = stringResource(R.string.ready),
                    fontSize = 14.sp
                )
            }
        }
    }
}

private fun String.addNumberMask(countAfterComma: Int): String {
    // Фильтруем ввод - только цифры и одна запятая
    val filtered = this.filter { it.isDigit() || it == ',' }
        .let { if (it.count { c -> c == ',' } > 1) it.replace(",", "") else it }

    // Разделяем на целую и дробную части
    val parts = filtered.split(',')
    val integerPart = parts[0].take(6) // ограничение на 6 цифр до запятой
    val decimalPart = parts.getOrNull(1)?.take(countAfterComma).orEmpty()

    // Собираем результат
    val result = if (decimalPart.isEmpty()) integerPart else "$integerPart,$decimalPart"
    return result
}

private class DecimalMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Не изменяем текст, просто отображаем как есть
        return TransformedText(text, OffsetMapping.Identity)
    }
}

fun Double.formatNumberWithLocale(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    formatter.maximumFractionDigits = 2
    return formatter.format(this)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun DialogBodyPreview() {
    SplitTheBillTheme {
        Column(
            modifier = Modifier
                .background(getColorTheme().primaryContainer)
        ) {
            AddNewItemDialogBody()
        }
    }
}