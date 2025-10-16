package com.raf.edcsimulation.sale.domain.usecase

import com.raf.edcsimulation.sale.domain.repository.SaleRepository

class SubmitSaleUseCase(private val saleRepository: SaleRepository) {
    suspend operator fun invoke(
        amount: Long,
        cardNumber: String,
        description: String?,
        merchantId: String,
    ) = saleRepository.submitSale(
        amount = amount,
        cardNumber = cardNumber,
        description = description,
        merchantId = merchantId,
    )
}