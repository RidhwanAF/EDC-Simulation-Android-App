package com.raf.edcsimulation.auth.domain.usecase

import com.raf.edcsimulation.auth.domain.models.LoginData
import com.raf.edcsimulation.auth.domain.repository.AuthRepository
import com.raf.edcsimulation.core.domain.model.APIResult
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Flow<APIResult<LoginData>> =
        authRepository.login(username, password)
}