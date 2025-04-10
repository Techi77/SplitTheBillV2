package com.stb.editlist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stb.theme.ui.getColorTheme

@Composable
fun ListMainScreen(
    listId: String = "",
    viewModel: ListMainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = getColorTheme().primaryContainer,
    ) { paddingValues ->
        EmptyList(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {

}