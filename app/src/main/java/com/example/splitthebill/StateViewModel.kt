package com.example.splitthebill

import com.example.splitthebill.appBase.UiState
import kotlinx.coroutines.flow.StateFlow

interface StateViewModel<State : UiState> {
    val state: StateFlow<State>
}