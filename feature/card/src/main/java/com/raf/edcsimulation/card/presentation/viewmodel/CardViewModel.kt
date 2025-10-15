package com.raf.edcsimulation.card.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.edcsimulation.core.domain.model.CardType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor() : ViewModel() {

    val listCard = listOf(
        CardData(
            cardTitle = "Chip Card",
            cardType = CardType.CHIP,
            cardColor = Color(0xFFCDEDA3),
            cardTextColor = Color(0xFF354E16),
        ),
        CardData(
            cardTitle = "Contact Less Card",
            cardType = CardType.CONTACT_LESS,
            cardColor = Color(0xFF284777),
            cardTextColor = Color(0xFFD6E3FF),
        ),
        CardData(
            cardTitle = "Manual Card",
            cardType = CardType.MANUAL,
            cardColor = Color(0xFFFFB77C),
            cardTextColor = Color(0xFF4D2700),
        ),
    )

    private val _uiState = MutableStateFlow(CardUiState())
    val uiState = _uiState.asStateFlow()

    var cardNumber by mutableStateOf("")
        private set

    fun onCardNumberChange(cardNumber: String) {
        this.cardNumber = cardNumber
    }

    fun onCardTypeChange(cardType: CardType?) {
        _uiState.update {
            it.copy(cardType = cardType)
        }
    }

    fun processCard(onCardProcessed: () -> Unit) {
        viewModelScope.launch {
            val processMessage = when (uiState.value.cardType) {
                CardType.CHIP -> "Reading Chip..."
                CardType.CONTACT_LESS -> "Reading NFC Card..."
                else -> null
            }
            _uiState.update {
                it.copy(isLoading = true, message = processMessage)
            }
            delay(3000)
            _uiState.update {
                it.copy(isLoading = false, message = null)
            }
            onCardProcessed()
        }
    }

    fun processManualCard(onCardProcessed: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = "$cardNumber\nProcessing Card..."
            )
            delay(3000)
            _uiState.value = _uiState.value.copy(isLoading = false, message = null)
            onCardProcessed()
        }
    }
}