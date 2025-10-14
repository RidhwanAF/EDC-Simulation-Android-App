package com.raf.edcsimulation.core.domain.repository

interface AuthRepository {
    suspend fun register(email: String, password: String, confirmPassword: String)
    suspend fun login(email: String, password: String)
    suspend fun logout()
}