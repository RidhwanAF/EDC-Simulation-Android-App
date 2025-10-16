package com.raf.edcsimulation.card.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import com.raf.edcsimulation.core.domain.model.AppSettings
import com.raf.edcsimulation.core.domain.model.CardType

data class CardUiState(
    val appSettings: AppSettings = AppSettings(),
    val cardType: CardType? = null,
    val message: String? = null,
    val isLoading: Boolean = false,
)

data class CardData(
    val cardTitle: String = "",
    val cardType: CardType,
    val cardColor: Color,
    val cardTextColor: Color,
    val cardNumber: String? = null,
)
