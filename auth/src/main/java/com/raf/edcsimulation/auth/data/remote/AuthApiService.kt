package com.raf.edcsimulation.auth.data.remote

import com.raf.edcsimulation.auth.data.models.LoginResponse
import com.raf.edcsimulation.auth.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(): Response<LoginResponse>
}