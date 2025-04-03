package com.stb.main

import com.stb.appbase.UiState

data class  MainUiState (
    val list: List<ListItem> = emptyList()
) : UiState {
    data class ListItem (
        val id: String,
        val title: String,
        val subtitle: String,
        val date: String,
        val iconId: Int
    )
}