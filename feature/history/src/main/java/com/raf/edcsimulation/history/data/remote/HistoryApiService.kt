package com.raf.edcsimulation.history.data.remote

import com.raf.edcsimulation.history.data.models.HistoryItemData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HistoryApiService {
    @GET("transactions/history")
    suspend fun getHistory(
        @Header("Authorization") token: String,
    ): Response<List<HistoryItemData>>
}