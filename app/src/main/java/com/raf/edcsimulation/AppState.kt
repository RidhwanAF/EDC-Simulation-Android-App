package com.raf.edcsimulation

sealed class AppState {
    object Loading : AppState()
    data class Loaded(val isLoggedIn: Boolean) : AppState()
}