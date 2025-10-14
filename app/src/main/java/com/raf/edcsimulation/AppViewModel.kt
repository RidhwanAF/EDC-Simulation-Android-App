package com.raf.edcsimulation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.edcsimulation.core.domain.usecase.GetTokenSessionUseCase
import com.raf.edcsimulation.core.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val getTokenSessionUseCase: GetTokenSessionUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val appState: StateFlow<AppState> = getTokenSessionUseCase()
        .map {
            Log.d("AppViewModel", "uiState: $it")
            AppState.Loaded(isLoggedIn = it != null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AppState.Loading,
        )

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAppSettings()
    }

    private fun loadAppSettings() {
        viewModelScope.launch {
            // TODO
            _uiState.update { currentState ->
                currentState.copy(
                    isDarkTheme = false,
                    isDynamicColor = false
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}