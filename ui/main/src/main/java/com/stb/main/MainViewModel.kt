package com.stb.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.stb.appbase.BaseViewModel
import com.stb.preferences.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : BaseViewModel<MainUiState, MainUiEvent>() {
    override fun createInitialState() = MainUiState()

    companion object {
        const val LIST_ID = "list_it"
    }

    private val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(application.applicationContext)
    }

    fun logout() {
        viewModelScope.launch { dataStoreManager.clearUser() }
    }

    fun goToEditOrCreationList(listId: String){
        pushEvent(MainUiEvent.NavigateToEditList(listId))
    }
}