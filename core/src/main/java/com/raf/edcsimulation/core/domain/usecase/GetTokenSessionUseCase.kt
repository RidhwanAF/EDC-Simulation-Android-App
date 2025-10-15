package com.raf.edcsimulation.core.domain.usecase

import com.raf.edcsimulation.core.domain.contracts.AuthTokenProvider

class GetTokenSessionUseCase(private val authTokenProvider: AuthTokenProvider) {
    operator fun invoke() = authTokenProvider.getAuthTokenSession()
}