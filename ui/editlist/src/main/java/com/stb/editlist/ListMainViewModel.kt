package com.stb.editlist

import com.stb.appbase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListMainViewModel @Inject constructor() : BaseViewModel<ListMainUiState, Nothing>() {
    override fun createInitialState() = ListMainUiState()

    fun changeFileName(fileName: String) =
        updateState {
            copy(
                fileName = fileName
            )
        }

    fun setNewItem(
        newItem: ListMainUiState.ListItem? = null
    ) {
        updateState {
            copy(
                newItem = newItem
            )
        }
    }
    fun saveNewItem() {
        state.value.newItem?.let {
            updateState {
                copy(
                    list = list + it,
                    newItem = null
                )
            }
        } ?: updateState {
            copy(
                showNewItemError = true
            )
        }
    }
}