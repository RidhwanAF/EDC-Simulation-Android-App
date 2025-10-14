package com.raf.edcsimulation.auth.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.edcsimulation.auth.domain.usecase.LoginUseCase
import com.raf.edcsimulation.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    var showConfirmPassword by mutableStateOf(false)
        private set

    fun onEmailChange(value: String) {
        email = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    fun onConfirmPasswordChange(value: String) {
        confirmPassword = value
    }

    fun onShowPasswordChange(value: Boolean) {
        showPassword = value
    }

    fun onShowConfirmPasswordChange(value: Boolean) {
        showConfirmPassword = value
    }

    fun resetUserInputData() {
        email = ""
        password = ""
        confirmPassword = ""
        showPassword = false
        showConfirmPassword = false
    }

    /**
     * Register new user
     */
    fun register() {
        viewModelScope.launch {
            registerUseCase(email, password, confirmPassword)
        }
    }

    /**
     * Login user
     */
    fun login() {
        viewModelScope.launch {
            loginUseCase(email, password)
        }
    }
}