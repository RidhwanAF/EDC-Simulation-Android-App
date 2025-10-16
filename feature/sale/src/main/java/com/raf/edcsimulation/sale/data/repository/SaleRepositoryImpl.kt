package com.raf.edcsimulation.sale.data.repository

import com.raf.edcsimulation.core.domain.contracts.AuthTokenProvider
import com.raf.edcsimulation.core.domain.model.APIResult
import com.raf.edcsimulation.sale.data.model.SaleBody
import com.raf.edcsimulation.sale.data.remote.SaleApiService
import com.raf.edcsimulation.sale.domain.model.SaleData
import com.raf.edcsimulation.sale.domain.repository.SaleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val authTokenProvider: AuthTokenProvider,
    private val saleApiService: SaleApiService,
) : SaleRepository {
    override suspend fun submitSale(
        amount: Long,
        cardNumber: String,
        description: String?,
        merchantId: String,
    ): Flow<APIResult<SaleData>> = flow {
        emit(APIResult.Loading)
        val response = saleApiService.submitSale(
            token = "Bearer ${authTokenProvider.getAuthTokenSession().first()}",
            saleBody = SaleBody(
                amount = amount,
                cardNumber = cardNumber,
                description = description,
                merchantId = merchantId,
            )
        )
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(
                    APIResult.Success(
                        SaleData(
                            status = body.status ?: "Unknown",
                            transactionId = body.transactionId ?: "Unknown",
                        )
                    )
                )
            } else {
                emit(APIResult.Error("Unknown error"))
            }
        } else {
            emit(APIResult.Error("Unknown error"))
        }
    }
}