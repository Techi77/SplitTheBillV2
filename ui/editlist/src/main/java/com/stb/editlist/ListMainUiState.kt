package com.stb.editlist

import com.stb.appbase.UiState
import java.util.Random

data class ListMainUiState (
    val list: List<ListItem> = emptyList(),
    val fileName: String = "",
    val newItem: ListItem? = null,
    val showNewItemError: Boolean = false
) : UiState {
    data class ListItem (
        val id: Int = Random().nextInt(),
        val title: String = "",
        val quantityOrWeight: Double = 1.0,
        val pricePerUnit: Double = 0.0,
    )
}