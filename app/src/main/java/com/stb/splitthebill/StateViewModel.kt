package com.stb.splitthebill

import com.stb.splitthebill.appBase.UiState
import kotlinx.coroutines.flow.StateFlow

interface StateViewModel<State : UiState> {
    val state: StateFlow<State>
}