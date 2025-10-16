package com.raf.edcsimulation.settlement.data.repository

import com.raf.edcsimulation.core.domain.contracts.AuthTokenProvider
import com.raf.edcsimulation.core.domain.model.APIResult
import com.raf.edcsimulation.settlement.data.remote.SettlementApiService
import com.raf.edcsimulation.settlement.domain.models.SettlementData
import com.raf.edcsimulation.settlement.domain.repository.SettlementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettlementRepositoryImpl @Inject constructor(
    private val authTokenProvider: AuthTokenProvider,
    private val settlementApiService: SettlementApiService,
) : SettlementRepository {

    override suspend fun processSettlement(): Flow<APIResult<SettlementData>> = flow {
        try {
            emit(APIResult.Loading)
            val authToken = authTokenProvider.getAuthTokenSession().firstOrNull()
                ?: emit(APIResult.Error("Unauthorized"))
            val token = "Bearer $authToken"
            val apiResponse = settlementApiService.processSettle(token)

            if (apiResponse.isSuccessful) {
                val responseBody = apiResponse.body()
                if (responseBody != null) {
                    emit(
                        APIResult.Success(
                            SettlementData(
                                message = responseBody.message ?: "",
                                recordsUpdated = responseBody.recordsUpdated ?: 0
                            )
                        )
                    )
                } else {
                    emit(APIResult.Error("Error while processing data"))
                }
            } else {
                emit(APIResult.Error(apiResponse.message()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(APIResult.Error("Error while processing data"))
        }
    }
}