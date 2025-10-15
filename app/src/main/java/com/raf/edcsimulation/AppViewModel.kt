package com.raf.edcsimulation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.edcsimulation.core.domain.usecase.GetAppSettingsUseCase
import com.raf.edcsimulation.core.domain.usecase.GetTokenSessionUseCase
import com.raf.edcsimulation.core.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    getTokenSessionUseCase: GetTokenSessionUseCase,
    getAppSettingsUseCase: GetAppSettingsUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    val appState: StateFlow<AppState> = combine(
        getTokenSessionUseCase(),
        getAppSettingsUseCase()
    ) { tokenSession, appSettings ->
        Log.d(TAG, "uiState: isLoggedIn=${tokenSession != null}, appSettings=$appSettings")
        AppState.Loaded(isLoggedIn = tokenSession != null, appSettings = appSettings)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AppState.Loading,
    )

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    companion object {
        private const val TAG = "AppViewModel"
    }
}