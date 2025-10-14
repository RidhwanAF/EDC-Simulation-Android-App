package com.raf.edcsimulation.auth.data.remote

import com.raf.edcsimulation.auth.data.models.LoginRequest
import com.raf.edcsimulation.auth.data.models.LoginResponse
import com.raf.edcsimulation.auth.data.models.RegisterRequest
import com.raf.edcsimulation.auth.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}