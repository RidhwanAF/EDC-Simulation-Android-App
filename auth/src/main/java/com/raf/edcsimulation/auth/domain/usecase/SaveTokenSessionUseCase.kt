package com.raf.edcsimulation.auth.domain.usecase

import com.raf.edcsimulation.auth.domain.repository.AuthRepository

class SaveTokenSessionUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(value: String) {
        authRepository.saveTokenSession(value)
    }
}