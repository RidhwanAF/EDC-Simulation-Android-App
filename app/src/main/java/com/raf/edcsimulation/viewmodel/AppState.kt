package com.raf.edcsimulation.viewmodel

import com.raf.edcsimulation.core.domain.model.AppSettings

sealed class AppState {
    object Loading : AppState()
    data class Loaded(val isLoggedIn: Boolean, val appSettings: AppSettings) : AppState()
}