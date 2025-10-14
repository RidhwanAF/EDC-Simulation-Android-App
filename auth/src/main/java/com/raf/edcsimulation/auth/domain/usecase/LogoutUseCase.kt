package com.raf.edcsimulation.auth.domain.usecase

import com.raf.edcsimulation.core.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}