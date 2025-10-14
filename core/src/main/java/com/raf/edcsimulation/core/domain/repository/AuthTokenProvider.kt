package com.raf.edcsimulation.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthTokenProvider {
    fun getAuthTokenSession(): Flow<String?>
    suspend fun logout()
}