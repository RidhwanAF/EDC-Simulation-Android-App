package com.raf.edcsimulation.sale.data.remote

import com.raf.edcsimulation.sale.data.models.SaleBody
import com.raf.edcsimulation.sale.data.models.SaleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SaleApiService {
    @POST("transactions/sale")
    suspend fun submitSale(
        @Header("Authorization") token: String,
        @Body saleBody: SaleBody,
    ): Response<SaleResponse>
}