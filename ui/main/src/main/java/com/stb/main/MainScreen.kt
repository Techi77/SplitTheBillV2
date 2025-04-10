package com.stb.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stb.appbase.CollectAsEventWithLifecycle
import com.stb.components.RegistrationDialogButton
import com.stb.theme.ui.Border
import com.stb.theme.ui.BorderDark
import com.stb.theme.ui.MainBlue
import com.stb.theme.ui.SplitTheBillTheme
import com.stb.theme.ui.getColorTheme
import com.stb.ui.main.R
import com.stb.components.R as MainR
import com.stb.theme.R as ThemeR

@Composable
fun MainScreen(
    navigateToListDetail: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.events.CollectAsEventWithLifecycle {
        when (it) {
            is MainUiEvent.NavigateToEditList -> {
                navigateToListDetail(it.listId)
            }
        }
    }
    MainScreenScaffold(
        state = state,
        actionHandler = remember {
            { action ->
                when (action) {
                    MainScreenAction.Logout -> viewModel.logout()
                    is MainScreenAction.CreateOrEditList -> {
                        viewModel.goToEditOrCreationList(action.listId)
                    }
                }
            }
        }
    )
}

@Composable
private fun MainScreenScaffold(
    state: MainUiState,
    actionHandler: (MainScreenAction) -> Unit = {},
) {
    Scaffold(
        containerColor = getColorTheme().primaryContainer,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = MainBlue,
                    scrolledContainerColor = MainBlue,
                    navigationIconContentColor = getColorTheme().onSecondary,
                    titleContentColor = getColorTheme().onSecondary,
                    actionIconContentColor = getColorTheme().onSecondary
                ),
                title = {
                    Text(
                        text = stringResource(ThemeR.string.app_name)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            actionHandler.invoke(MainScreenAction.Logout)
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    actionHandler.invoke(MainScreenAction.CreateOrEditList(""))
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
        if (state.list.isEmpty())
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EmptyMainScreenBody(
                    modifier = Modifier
                        .padding(paddingValues),
                    onGoButtonClick = {
                        actionHandler.invoke(MainScreenAction.CreateOrEditList(""))
                    }
                )
            }
        else
            MainScreenBody(
                modifier = Modifier.padding(paddingValues),
                state = state,
                onItemClick = {
                    actionHandler.invoke(MainScreenAction.CreateOrEditList(it))
                }
            )
    }
}

@Composable
private fun EmptyMainScreenBody(
    modifier: Modifier = Modifier,
    onGoButtonClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(28.dp)
    Column(
        modifier = modifier
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
        Image(
            bitmap = ImageBitmap.imageResource(MainR.drawable.ic_pizza),
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
        )
        Text(
            text = stringResource(R.string.create),
            color = getColorTheme().onPrimaryContainer,
            fontSize = 24.sp
        )
        Text(
            text = stringResource(R.string.your_first_list),
            color = getColorTheme().onPrimaryContainer,
            fontSize = 14.sp
        )
        RegistrationDialogButton(
            text = stringResource(R.string.go),
            onClick = onGoButtonClick
        )
    }
}

@Composable
private fun MainScreenBody(
    modifier: Modifier = Modifier,
    state: MainUiState,
    onItemClick: (String) -> Unit = {}
) {
    LazyColumn(modifier = modifier) {
        items(items = state.list, key = { it.id }) {
            ListItem(
                itemData = it,
                modifier = Modifier.animateItem(),
                onItemClick = { onItemClick(it.id) }
            )
        }
    }
}

@Composable
private fun ListItem(
    itemData: MainUiState.ListItem,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.clickable {
            onItemClick()
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = getListItemById(itemData.iconId),
                contentDescription = "",
                modifier = Modifier
                    .size(56.dp)
            )
            Column(
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, start = 16.dp)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = itemData.title,
                    fontSize = 16.sp,
                    color = getColorTheme().onPrimaryContainer,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = itemData.subtitle,
                    fontSize = 14.sp,
                    color = getColorTheme().onPrimaryContainer,
                    letterSpacing = 0.25.sp
                )
            }
        }
        Text(
            text = itemData.date, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 6.dp),
            fontSize = 10.sp,
            color = getColorTheme().onPrimaryContainer
        )
        HorizontalDivider(
            color = getColorTheme().outline,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun getListItemById(iconId: Int) = when (iconId) {
    0 -> painterResource(R.drawable.ic_list_item_0)
    1 -> painterResource(R.drawable.ic_list_item_1)
    2 -> painterResource(R.drawable.ic_list_item_2)
    3 -> painterResource(R.drawable.ic_list_item_3)
    else -> painterResource(R.drawable.ic_list_item_4)
}

@Immutable
private sealed interface MainScreenAction {
    data object Logout : MainScreenAction
    data class CreateOrEditList(val listId: String) : MainScreenAction
}

private val previewItem = MainUiState.ListItem(
    id = "111",
    title = "Список1",
    subtitle = "Доля: 500 (из 7000)",
    date = "01/01/2022",
    iconId = 0
)

@Composable
@Preview
private fun EmptyMainScreenPreview() {
    SplitTheBillTheme {
        MainScreenScaffold(
            state = MainUiState(
                list = listOf()
            )
        )
    }
}

@Composable
@Preview
private fun MainScreenPreview() {
    SplitTheBillTheme {
        MainScreenScaffold(
            state = MainUiState(
                list = List(10) { previewItem.copy(id = it.toString()) }
            )
        )
    }
}

@Composable
@Preview
private fun MainScreenLisItemPreview() {
    SplitTheBillTheme {
        Column(
            modifier = Modifier.background(getColorTheme().primaryContainer)
        ) {
            ListItem(
                previewItem
            )
        }
    }
}
