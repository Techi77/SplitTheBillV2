package com.stb.appbase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Event : UiEvent> :
    ViewModel(), StateViewModel<State> {

    private val initialState: State by lazy { createInitialState() }
    abstract fun createInitialState(): State

    private val uiState: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }
    override val state by lazy { uiState.asStateFlow() }

    private val _events = Channel<Event>(2)
    val events = _events.receiveAsFlow()

    protected fun pushEvent(vararg events: Event) {
        viewModelScope.launch {
            events.forEach { _events.send(it) }
        }
    }

    protected fun updateState(newState: State.() -> State) {
        uiState.update {
            newState.invoke(it)
        }
    }
}