package com.raf.edcsimulation.auth.presentation.viewmodel

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val usernameError: Boolean = false,
    val usernameErrorMessage: String = "",
    val passwordError: Boolean = false,
    val passwordErrorMessage: String = "",
    val confirmPasswordError: Boolean = false,
    val confirmPasswordErrorMessage: String = "",
    val isButtonEnabled: Boolean = false,
    val loginState: AuthState = AuthState.Idle,
    val registerState: AuthState = AuthState.Idle
)
