package com.stb.main

import com.stb.appbase.UiState

data class  MainUiState (
    val list: List<String> = emptyList()
) : UiState