package com.raf.edcsimulation.core.domain.usecase

import com.raf.edcsimulation.core.domain.repository.AuthTokenProvider

class LogoutUseCase(private val authTokenProvider: AuthTokenProvider) {
    suspend operator fun invoke() {
        authTokenProvider.logout()
    }
}