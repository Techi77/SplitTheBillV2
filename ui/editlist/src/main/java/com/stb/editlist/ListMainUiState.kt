package com.stb.editlist

import com.stb.appbase.UiState
import com.stb.editlist.entity.ListItem

data class ListMainUiState (
    val list: List<ListItem> = emptyList(),
    val fileName: String = "",
    val showAddNewItemDialog: Boolean = false,
    val showNewItemError: Boolean = false
) : UiState