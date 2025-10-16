package com.raf.edcsimulation.settlement.data.remote

import com.raf.edcsimulation.settlement.data.models.SettlementResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface SettlementApiService {
    @POST("transactions/settlement")
    suspend fun processSettle(
        @Header("Authorization") token: String,
    ): Response<SettlementResponse>
}