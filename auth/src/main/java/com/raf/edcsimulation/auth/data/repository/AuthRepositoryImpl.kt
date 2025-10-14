package com.raf.edcsimulation.auth.data.repository

import android.util.Log
import com.raf.edcsimulation.auth.data.local.AuthDataStore
import com.raf.edcsimulation.auth.data.remote.AuthApiService
import com.raf.edcsimulation.core.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val authApiService: AuthApiService,
) : AuthRepository {

    override suspend fun register(
        email: String,
        password: String,
        confirmPassword: String,
    ) {
        try {
            authApiService.register()
        } catch (e: Exception) {
            Log.e(TAG, "register: ${e.message}")
        }
    }

    override suspend fun login(email: String, password: String) {
        try {
            authApiService.login()
        } catch (e: Exception) {
            Log.e(TAG, "login: ${e.message}")
        }
    }

    override suspend fun logout() {
        try {
            authDataStore.clearJwtToken()
        } catch (e: Exception) {
            Log.e(TAG, "logout: ${e.message}")
        }
    }

    private companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}