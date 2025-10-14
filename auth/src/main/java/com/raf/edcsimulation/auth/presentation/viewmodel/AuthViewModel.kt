package com.raf.edcsimulation.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.edcsimulation.auth.domain.usecase.LoginUseCase
import com.raf.edcsimulation.auth.domain.usecase.RegisterUseCase
import com.raf.edcsimulation.auth.domain.usecase.SaveTokenSessionUseCase
import com.raf.edcsimulation.core.domain.model.APIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val saveTokenSessionUseCase: SaveTokenSessionUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onInputChange(
        username: String? = null,
        password: String? = null,
        confirmPassword: String? = null,
        showPassword: Boolean? = null,
        showConfirmPassword: Boolean? = null,
    ) {
        _uiState.update {
            it.copy(
                username = username ?: it.username,
                password = password ?: it.password,
                confirmPassword = confirmPassword ?: it.confirmPassword,
                showPassword = showPassword ?: it.showPassword,
                showConfirmPassword = showConfirmPassword ?: it.showConfirmPassword
            )
        }
    }

    fun validateUsername(isRegisterForm: Boolean) {
        val isUsernameValid = if (isRegisterForm) uiState.value.username.length >= 3
        else true

        _uiState.update {
            it.copy(
                usernameError = !isUsernameValid,
                usernameErrorMessage = if (isUsernameValid) "" else "Username must be at least 3 characters"
            )
        }
    }

    fun validatePassword(isRegisterForm: Boolean) {
        val password = uiState.value.password
        val hasLength = password.length >= 8
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        val isPasswordValid =
            if (isRegisterForm) hasLength && hasUppercase && hasLowercase && hasDigit && hasSpecialChar
            else true

        _uiState.update {
            it.copy(
                passwordError = !isPasswordValid,
                passwordErrorMessage = if (isPasswordValid) ""
                else "Password must be at least 8 characters, including uppercase, lowercase, number, and special character"
            )
        }
    }

    fun validateConfirmPassword() {
        val isConfirmPasswordValid = uiState.value.confirmPassword == uiState.value.password
        _uiState.update {
            it.copy(
                confirmPasswordError = !isConfirmPasswordValid,
                confirmPasswordErrorMessage = if (isConfirmPasswordValid) "" else "Passwords do not match"
            )
        }
    }

    fun validateButton(isRegisterForm: Boolean) {
        val isButtonEnabled = if (isRegisterForm) {
            !uiState.value.usernameError && !uiState.value.passwordError && !uiState.value.confirmPasswordError
                    && !uiState.value.username.isBlank() && !uiState.value.password.isBlank() && !uiState.value.confirmPassword.isBlank()
        } else {
            !uiState.value.usernameError && !uiState.value.passwordError
                    && !uiState.value.username.isBlank() && !uiState.value.password.isBlank()
        }
        _uiState.update {
            it.copy(
                isButtonEnabled = isButtonEnabled
            )
        }
    }

    fun resetError() {
        _uiState.update {
            it.copy(
                usernameError = false,
                passwordError = false,
                confirmPasswordError = false,
                usernameErrorMessage = "",
                passwordErrorMessage = "",
                confirmPasswordErrorMessage = ""
            )
        }
    }

    fun resetAuthState() {
        _uiState.update {
            it.copy(
                loginState = AuthState.Idle,
                registerState = AuthState.Idle
            )
        }
    }

    fun resetUiState() {
        _uiState.update { AuthUiState() }
    }

    /**
     * Register new user
     */
    fun register() {
        viewModelScope.launch {
            registerUseCase(
                username = uiState.value.username,
                password = uiState.value.confirmPassword
            ).collect { result ->
                when (result) {
                    is APIResult.Success -> {
                        _uiState.update {
                            it.copy(
                                registerState = AuthState.Success(result.data.message)
                            )
                        }
                    }

                    is APIResult.Error -> {
                        _uiState.update {
                            it.copy(
                                registerState = AuthState.Error(result.message)
                            )
                        }
                    }

                    is APIResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                registerState = AuthState.Loading
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Login user
     */
    fun login() {
        viewModelScope.launch {
            loginUseCase(
                username = uiState.value.username,
                password = uiState.value.password
            ).collect { result ->
                when (result) {
                    is APIResult.Success -> {
                        saveTokenSessionUseCase(result.data.token)
                        _uiState.update {
                            it.copy(
                                loginState = AuthState.Success(result.data.message)
                            )
                        }
                    }

                    is APIResult.Error -> {
                        _uiState.update {
                            it.copy(
                                loginState = AuthState.Error(result.message)
                            )
                        }
                    }

                    is APIResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                loginState = AuthState.Loading
                            )
                        }
                    }
                }
            }
        }
    }
}