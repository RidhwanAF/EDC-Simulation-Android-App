package com.raf.edcsimulation.history.data.repository

import android.util.Log
import com.raf.edcsimulation.core.domain.contracts.AuthTokenProvider
import com.raf.edcsimulation.core.domain.model.APIResult
import com.raf.edcsimulation.history.data.local.HistoryDataStore
import com.raf.edcsimulation.history.data.models.HistoryItemData
import com.raf.edcsimulation.history.data.remote.HistoryApiService
import com.raf.edcsimulation.history.data.repository.mappers.HistoryDataToDomain.toDomain
import com.raf.edcsimulation.history.domain.models.HistoryData
import com.raf.edcsimulation.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val authTokenProvider: AuthTokenProvider,
    private val historyApiService: HistoryApiService,
    private val historyDataStore: HistoryDataStore,
) : HistoryRepository {

    val json = Json { ignoreUnknownKeys = true }

    override fun fetchHistory(): Flow<APIResult<List<HistoryData>>> = flow {
        try {
            emit(APIResult.Loading)
            val token = authTokenProvider.getAuthTokenSession().firstOrNull()
                ?: emit(APIResult.Error("Unauthorized"))
            val response = historyApiService.getHistory("Bearer $token")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.isNotEmpty()) {
                    val jsonString = try {
                        json.encodeToString(ListSerializer(HistoryItemData.serializer()), body)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error encoding history data to JSON", e)
                        null
                    }

                    emit(APIResult.Success(body.map { it.toDomain() }))
                    jsonString?.let {
                        historyDataStore.saveLastHistory(it)
                    }
                } else {
                    emit(APIResult.Error(response.message()))
                }
            } else {
                emit(APIResult.Error(response.message()))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching history", e)
            emit(APIResult.Error(e.message ?: "Unknown error"))
        }
    }

    override fun getHistoryLocal(): Flow<List<HistoryData>> = flow {
        val jsonString = historyDataStore.getLastHistory().firstOrNull()
        if (jsonString != null) {
            val historyData = try {
                json.decodeFromString(ListSerializer(HistoryItemData.serializer()), jsonString)
            } catch (e: Exception) {
                Log.e(TAG, "Error decoding history data from JSON", e)
                null
            }
            if (historyData != null) {
                emit(historyData.map { it.toDomain() })
            } else {
                emit(emptyList())
            }
        } else {
            emit(emptyList())
        }
    }

    private companion object {
        private const val TAG = "HistoryRepositoryImpl"
    }
}