package com.stb.editlist

import android.app.Application
import com.stb.appbase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListMainViewModel @Inject constructor(
    application: Application
) : BaseViewModel<ListMainUiState, Nothing>() {
    override fun createInitialState() = ListMainUiState()
}