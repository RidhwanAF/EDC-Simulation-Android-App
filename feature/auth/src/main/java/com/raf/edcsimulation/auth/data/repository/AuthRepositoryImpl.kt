package com.raf.edcsimulation.auth.data.repository

import android.util.Base64
import android.util.Log
import com.raf.edcsimulation.auth.BuildConfig
import com.raf.edcsimulation.auth.data.local.AuthDataStore
import com.raf.edcsimulation.auth.data.models.ErrorResponse
import com.raf.edcsimulation.auth.data.models.LoginRequest
import com.raf.edcsimulation.auth.data.models.RegisterRequest
import com.raf.edcsimulation.auth.data.models.UserJWT
import com.raf.edcsimulation.auth.data.remote.AuthApiService
import com.raf.edcsimulation.auth.domain.models.LoginData
import com.raf.edcsimulation.auth.domain.models.RegisterData
import com.raf.edcsimulation.auth.domain.repository.AuthRepository
import com.raf.edcsimulation.core.data.utils.SessionEncryptionManager
import com.raf.edcsimulation.core.domain.contracts.AuthTokenProvider
import com.raf.edcsimulation.core.domain.model.APIResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val authApiService: AuthApiService,
) : AuthTokenProvider, AuthRepository {

    override suspend fun register(
        email: String,
        password: String,
    ): Flow<APIResult<RegisterData>> = flow {
        emit(APIResult.Loading)
        try {
            val request = RegisterRequest(email, password)
            val response = authApiService.register(request)

            if (!response.isSuccessful) {
                when (response.code()) {
                    400 -> {
                        val json = Json { ignoreUnknownKeys = true }
                        val parsedErrorResponse = json.decodeFromString<ErrorResponse>(
                            response.errorBody()?.string() ?: ""
                        )
                        emit(APIResult.Error(parsedErrorResponse.error ?: "Unknown error"))
                    }

                    else -> emit(APIResult.Error("Unknown error"))
                }
            } else {
                emit(APIResult.Success(RegisterData(response.body()?.message ?: "")))
            }
        } catch (e: Exception) {
            emit(APIResult.Error(e.message ?: "Unknown error"))
            Log.e(TAG, "register: ${e.message}")
        }
    }

    override suspend fun login(email: String, password: String): Flow<APIResult<LoginData>> = flow {
        emit(APIResult.Loading)
        try {
            val request = LoginRequest(email, password)
            val response = authApiService.login(request)
            if (!response.isSuccessful) {
                when (response.code()) {
                    401 -> {
                        val json = Json { ignoreUnknownKeys = true }
                        val parsedErrorResponse = json.decodeFromString<ErrorResponse>(
                            response.errorBody()?.string() ?: ""
                        )
                        emit(APIResult.Error(parsedErrorResponse.error ?: "Unknown error"))
                    }

                    else -> emit(APIResult.Error("Unknown error"))
                }
            } else {
                val token = response.body()?.token
                emit(
                    APIResult.Success(
                        LoginData(response.body()?.message ?: "Login success")
                    )
                )
                // Save Token
                if (token != null) {
                    saveTokenSession(token)
                }
            }
        } catch (e: Exception) {
            emit(APIResult.Error(e.message ?: "Unknown error"))
            Log.e(TAG, "login: ${e.message}")
        }
    }

    override suspend fun logout() {
        try {
            authDataStore.clearJwtToken()
            Log.d(TAG, "logout: Success")
        } catch (e: Exception) {
            Log.e(TAG, "logout: ${e.message}")
        }
    }

    private suspend fun saveTokenSession(value: String) {
        val encryptedToken =
            SessionEncryptionManager.encrypt(value, BuildConfig.SECRET_KEY_STRING) ?: return
        authDataStore.saveJwtToken(encryptedToken)
    }

    override fun getAuthTokenSession(): Flow<String?> {
        val encryptedToken = authDataStore.getJwtToken()
        return encryptedToken.map { token ->
            token?.let { SessionEncryptionManager.decrypt(it, BuildConfig.SECRET_KEY_STRING) }
        }
    }

    override suspend fun getUserId(): String? {
        return try {
            val authToken = getAuthTokenSession().firstOrNull() ?: return null
            // Split the JWT into its three parts (header, payload, signature)
            val parts = authToken.split(".")
            if (parts.size != 3) {
                Log.e(TAG, "getUserId: Invalid JWT format")
                return null
            }
            // Decode the Base64Url-encoded payload (the second part)
            val payload = parts[1]
            val decodedPayloadBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedPayloadString = String(decodedPayloadBytes, Charsets.UTF_8)

            val json = Json { ignoreUnknownKeys = true }
            val userJWT = json.decodeFromString<UserJWT>(decodedPayloadString)
            userJWT.userId.toString()
        } catch (e: Exception) {
            Log.e(TAG, "getUserId: ${e.message}")
            null
        }
    }

    private companion object Companion {
        private const val TAG = "AuthRepositoryImpl"
    }
}