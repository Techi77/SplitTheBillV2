package com.stb.editlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stb.components.BasicDialogCard
import com.stb.theme.ui.SplitTheBillTheme
import com.stb.theme.ui.getColorTheme
import com.stb.ui.editlist.R
import kotlinx.coroutines.launch

@Composable
fun ListMainScreen(
    listId: String = "", //TODO использовать
    viewModel: ListMainViewModel = hiltViewModel(),
    goBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ListMainScreenScaffold(
        state = state,
        actionHandler = remember {
            { action ->
                when (action) {
                    is ListScreenAction.SetNewItem -> viewModel.setNewItem(
                        action.newItem
                    )

                    is ListScreenAction.ChangeFileName -> viewModel.changeFileName(action.fileName)

                    ListScreenAction.GoBack -> goBack()
                    ListScreenAction.SaveNewItem -> viewModel.saveNewItem()
                }
            }
        }
    )
}

@Composable
private fun ListMainScreenScaffold(
    state: ListMainUiState,
    actionHandler: (ListScreenAction) -> Unit = {},
) {
    Scaffold(
        containerColor = getColorTheme().primaryContainer,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    TextField(
                        value = state.fileName,
                        onValueChange = {
                            actionHandler.invoke(
                                ListScreenAction.ChangeFileName(
                                    it
                                )
                            )
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        actionHandler.invoke(
                            ListScreenAction.GoBack
                        )
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                actions = {
                    MoreMenu()
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    actionHandler.invoke(
                        ListScreenAction.SetNewItem(ListMainUiState.ListItem())
                    )
                },
                colors = ButtonColors(
                    containerColor = getColorTheme().primary,
                    contentColor = getColorTheme().onPrimary,
                    disabledContainerColor = getColorTheme().outline,
                    disabledContentColor = getColorTheme().onPrimary,
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Logout")
            }
        }
    ) { paddingValues ->
        if (state.newItem!=null) {
            AddNewItemDialog(
                state = state,
                setNewItem = { actionHandler.invoke(ListScreenAction.SetNewItem(it)) },
                saveItem = {actionHandler.invoke(ListScreenAction.SaveNewItem)}
            )
        } else
            EmptyListBody(
                modifier = Modifier.padding(paddingValues),
                actionHandler = actionHandler)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNewItemDialog(
    state: ListMainUiState,
    setNewItem: (ListMainUiState.ListItem?) -> Unit = {},
    saveItem: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showError = state.showNewItemError // todo доделать
    ModalBottomSheet(
        onDismissRequest = { setNewItem(null) },
        sheetState = sheetState
    ) {
        TextField(
            value = state.newItem?.title.orEmpty(),
            onValueChange = { setNewItem(state.newItem?.copy(
                title = it
            ) ?: ListMainUiState.ListItem()) },
            label = { Text(stringResource(R.string.name)) },
            isError = showError && state.newItem?.title.isNullOrBlank()
        )
        TextField(
            value = state.newItem?.pricePerUnit?.toString().orEmpty(),
            onValueChange = { setNewItem(state.newItem?.copy(
                pricePerUnit = it.toDoubleOrNull() ?: 0.0
            ) ?: ListMainUiState.ListItem()) },
            label = { Text(stringResource(R.string.price)) },
            isError = showError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
        TextField(
            value = state.newItem?.quantityOrWeight?.toString().orEmpty(),
            onValueChange = { setNewItem(state.newItem?.copy(
                quantityOrWeight = it.toDoubleOrNull() ?: 0.0
            ) ?: ListMainUiState.ListItem()) },
            label = { Text(stringResource(R.string.quantity)) },
            isError = showError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
        Button(onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    saveItem()
                }
            }
        }) {
            Text(stringResource(R.string.ready))
        }
    }
}

@Composable
private fun MoreMenu() {
    var showDropDownMenu by remember { mutableStateOf(true) } // TODO заменить на false
    IconButton(onClick = { showDropDownMenu = true }) {
        Icon(Icons.Filled.MoreVert, null)
    }
    DropdownMenu(
        expanded = showDropDownMenu,
        onDismissRequest = { showDropDownMenu = false }
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.members)) },
            onClick = { showDropDownMenu = false } // TODO дополнить
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.more_about)) },
            onClick = { showDropDownMenu = false } // TODO дополнить
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.archive_list)) },
            onClick = { showDropDownMenu = false } // TODO дополнить
        )
    }
}

@Composable
private fun EmptyListBody(
    modifier: Modifier = Modifier,
    actionHandler: (ListScreenAction) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterVertically)
    ) {
        BasicDialogCard(
            title = stringResource(R.string.invite),
            subtitle = stringResource(R.string.more_people),
            icon = ImageBitmap.imageResource(R.drawable.ic_happy_face),
            buttonText = stringResource(R.string.go),
            onButtonClick = {} //TODO заменить на нужное
        )
        BasicDialogCard(
            title = stringResource(R.string.add),
            subtitle = stringResource(R.string.things_you_bought),
            buttonText = stringResource(R.string.go),
            onButtonClick = {actionHandler.invoke(
                ListScreenAction.SetNewItem(ListMainUiState.ListItem())
            )}
        )
    }
}

@Immutable
private sealed interface ListScreenAction {
    data class SetNewItem(val newItem: ListMainUiState.ListItem?) : ListScreenAction
    data class ChangeFileName(val fileName: String) : ListScreenAction
    data object GoBack : ListScreenAction
    data object SaveNewItem: ListScreenAction
}

@Composable
@Preview
private fun EmptyListBodyPreview() {
    SplitTheBillTheme {
        EmptyListBody()
    }
}

@Composable
@Preview
private fun MoreMenuPreview() {
    SplitTheBillTheme {
        MoreMenu()
    }
}