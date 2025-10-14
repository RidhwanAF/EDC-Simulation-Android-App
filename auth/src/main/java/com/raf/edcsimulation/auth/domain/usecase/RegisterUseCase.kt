package com.raf.edcsimulation.auth.domain.usecase

import com.raf.edcsimulation.auth.domain.models.RegisterData
import com.raf.edcsimulation.auth.domain.repository.AuthRepository
import com.raf.edcsimulation.core.domain.model.APIResult
import kotlinx.coroutines.flow.Flow

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Flow<APIResult<RegisterData>> =
        authRepository.register(username, password)
}