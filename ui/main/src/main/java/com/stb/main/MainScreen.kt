package com.stb.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stb.theme.R as ThemeR

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MainScreenScaffold(
        state = state,
        actionHandler = remember {
            { action ->
                when (action) {
                    MainScreenAction.Logout -> viewModel.logout()
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
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
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

        ) { paddingValues ->
        MainScreenBody(
            modifier = Modifier.padding(paddingValues),
            state = state
        )
    }
}

@Composable
private fun MainScreenBody(
    modifier: Modifier = Modifier,
    state: MainUiState
) {
    LazyColumn(modifier = modifier) {

    }
}

@Immutable
private sealed interface MainScreenAction {
    data object Logout : MainScreenAction
}

@Composable
@Preview
private fun MainScreenPreview() {
    MainScreenScaffold(
        state = MainUiState()
    )
}