package com.raf.edcsimulation.card.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.edcsimulation.card.presentation.viewmodel.CardViewModel
import com.raf.edcsimulation.card.presentation.viewmodel.CardViewState
import com.raf.edcsimulation.core.domain.model.CardType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardMenuView(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: CardViewModel = hiltViewModel(),
    onCardProcessed: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var menuState by rememberSaveable {
        mutableStateOf(CardViewState.MENU_VIEW)
    }

    LaunchedEffect(menuState) {
        if (menuState == CardViewState.MENU_VIEW) {
            viewModel.onCardTypeChange(null)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = paddingValues.calculateTopPadding(),
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding()
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                AnimatedVisibility(menuState == CardViewState.MENU_VIEW) {
                    Text(
                        text = "Choose Card Type",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            items(viewModel.listCard.size) { index ->
                val cardData = viewModel.listCard[index]
                CardMenuItem(
                    cardData = cardData,
                    visible = cardData.cardType != uiState.cardType,
                    onMenuClicked = {
                        viewModel.onCardTypeChange(cardData.cardType)
                        menuState = CardViewState.CARD_VIEW
                    },
                    modifier = Modifier
                        .aspectRatio(3f / 4f)
                )
            }
        }

        CardItem(
            currentType = uiState.cardType,
            listCard = viewModel.listCard,
            visible = menuState == CardViewState.CARD_VIEW && uiState.cardType != CardType.MANUAL,
            onCardProcessed = { cardNumber ->
                viewModel.processCard(
                    onCardProcessed = {
                        onCardProcessed(cardNumber)
                    }
                )
            },
            onDismiss = {
                menuState = CardViewState.MENU_VIEW
            },
        )

        CardManualInput(
            paddingValues = paddingValues,
            visible = menuState == CardViewState.CARD_VIEW && uiState.cardType == CardType.MANUAL,
            cardData = viewModel.listCard.find { it.cardType == uiState.cardType }
                ?: viewModel.listCard.last(),
            cardNumberValue = viewModel.cardNumber,
            onCardNumberChange = {
                viewModel.onCardNumberChange(it)
            },
            onProcessCard = { cardNumber ->
                viewModel.processManualCard(
                    onCardProcessed = {
                        onCardProcessed(cardNumber)
                    }
                )
            },
            onDismiss = {
                menuState = CardViewState.MENU_VIEW
            },
        )

        /**
         * Processing Card Simulation
         */
        AnimatedVisibility(
            visible = uiState.isLoading && !uiState.message.isNullOrBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CircularProgressIndicator()
                uiState.message?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
