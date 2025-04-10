package com.stb.editlist

import com.stb.appbase.UiState

data class ListMainUiState (
    val list: List<String> = emptyList()
) : UiState