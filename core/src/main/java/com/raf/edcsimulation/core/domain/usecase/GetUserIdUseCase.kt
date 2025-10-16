package com.raf.edcsimulation.core.domain.usecase

import com.raf.edcsimulation.core.domain.contracts.AuthTokenProvider

class GetUserIdUseCase(private val authTokenProvider: AuthTokenProvider) {
    suspend operator fun invoke(): String? = authTokenProvider.getUserId()
}