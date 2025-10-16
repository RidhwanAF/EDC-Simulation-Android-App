package com.raf.edcsimulation.core.domain.contracts

import kotlinx.coroutines.flow.Flow

interface AuthTokenProvider {
    fun getAuthTokenSession(): Flow<String?>
    suspend fun logout()
    suspend fun getUserId(): String?
}