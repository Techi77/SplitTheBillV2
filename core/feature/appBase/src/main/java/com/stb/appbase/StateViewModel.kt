package com.stb.appbase

import kotlinx.coroutines.flow.StateFlow

interface StateViewModel<State : UiState> {
    val state: StateFlow<State>
}