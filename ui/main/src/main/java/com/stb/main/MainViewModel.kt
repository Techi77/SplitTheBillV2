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
) : BaseViewModel<MainUiState, Nothing>() {
    override fun createInitialState() = MainUiState()

    private val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(application.applicationContext)
    }

    fun logout() {
        viewModelScope.launch { dataStoreManager.clearUser() }
    }
}