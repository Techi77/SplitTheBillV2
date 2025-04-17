package com.stb.editlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stb.components.BasicDialogCard
import com.stb.editlist.entity.ListItem
import com.stb.theme.ui.SplitTheBillTheme
import com.stb.theme.ui.getColorTheme
import com.stb.ui.editlist.R

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
                    is ListScreenAction.AddNewItem -> viewModel.addNewItemToList(
                        action.newItem
                    )

                    is ListScreenAction.ChangeFileName -> viewModel.changeFileName(action.fileName)

                    ListScreenAction.GoBack -> goBack()
                    is ListScreenAction.ShowHideAddNewItemDialog -> viewModel.showHideAddNewItemDialog(
                        action.show
                    )
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
                        ListScreenAction.ShowHideAddNewItemDialog(true)
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
        if (state.showAddNewItemDialog) {
            AddNewItemDialog(
                addNewItem = { actionHandler.invoke(ListScreenAction.AddNewItem(it)) },
                hideDialog = { actionHandler.invoke(ListScreenAction.ShowHideAddNewItemDialog(false)) }
            )
        }
        if (state.list.isEmpty())
            EmptyListBody(
                modifier = Modifier.padding(paddingValues),
                actionHandler = actionHandler
            )
        else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = state.list, key = {it.id}) {
                    ItemFromList(it)
                }
            }
        }
    }
}

@Composable
private fun MoreMenu() {
    var showDropDownMenu by remember { mutableStateOf(false) }
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
            onButtonClick = {
                actionHandler.invoke(
                    ListScreenAction.ShowHideAddNewItemDialog(true)
                )
            }
        )
    }
}

@Composable
private fun ItemFromList(item: ListItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(getColorTheme().outline)
            .padding(1.dp)
            .background(getColorTheme().primaryContainer)
            .padding(15.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(50.dp),
            tint = getColorTheme().secondary.copy(alpha = 0.5f)
        )
        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            fontSize = 15.sp
        )
        Text(
            text = (item.pricePerUnit * item.quantityOrWeight).toString(),
            fontSize = 18.sp
        )
    }
}

@Immutable
private sealed interface ListScreenAction {
    data class AddNewItem(val newItem: ListItem) : ListScreenAction
    data class ChangeFileName(val fileName: String) : ListScreenAction
    data object GoBack : ListScreenAction
    data class ShowHideAddNewItemDialog(val show: Boolean) : ListScreenAction
}

@Composable
@Preview
private fun ListItemPreview() {
    SplitTheBillTheme {
        Column(
            modifier = Modifier
                .background(getColorTheme().primaryContainer)
        ) {
            ItemFromList(
                ListItem(
                    title = "Title",
                    quantityOrWeight = 1.0,
                    pricePerUnit = 1000.00
                )
            )
        }
    }
}

@Composable
@Preview
private fun FilledListBodyPreview() {
    SplitTheBillTheme {
        ListMainScreenScaffold(
            state = ListMainUiState(
                list = listOf(ListItem(
                    id = 0,
                    title = "Title",
                    quantityOrWeight = 1.0,
                    pricePerUnit = 1000.00
                ),
                    ListItem(
                        id = 2,
                        title = "Title",
                        quantityOrWeight = 1.0,
                        pricePerUnit = 1000.00
                    ),
                    ListItem(
                        id = 3,
                        title = "Title",
                        quantityOrWeight = 1.0,
                        pricePerUnit = 1000.00
                    ))
            )
        )
    }
}
@Composable
@Preview
private fun EmptyListBodyPreview() {
    SplitTheBillTheme {
        ListMainScreenScaffold(
            state = ListMainUiState()
        )
    }
}