package com.raf.edcsimulation.auth.domain.usecase

import com.raf.edcsimulation.core.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) {
        authRepository.login(email, password)
    }
}