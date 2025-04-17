package com.stb.editlist

import com.stb.appbase.BaseViewModel
import com.stb.editlist.entity.ListItem
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

    fun addNewItemToList(
        newItem: ListItem
    ) {
        updateState {
            copy(
                list = list + newItem
            )
        }
    }

    fun showHideAddNewItemDialog(show: Boolean) = updateState {
        copy(
            showAddNewItemDialog = show
        )
    }
}