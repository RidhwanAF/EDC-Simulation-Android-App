package com.raf.edcsimulation.auth.domain.repository

import com.raf.edcsimulation.auth.domain.models.LoginData
import com.raf.edcsimulation.auth.domain.models.RegisterData
import com.raf.edcsimulation.core.domain.model.APIResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(email: String, password: String): Flow<APIResult<RegisterData>>
    suspend fun login(email: String, password: String): Flow<APIResult<LoginData>>
}