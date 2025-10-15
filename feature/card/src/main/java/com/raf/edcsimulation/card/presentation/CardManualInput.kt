package com.raf.edcsimulation.card.presentation

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.raf.edcsimulation.card.presentation.viewmodel.CardData
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardManualInput(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    visible: Boolean,
    cardData: CardData,
    cardNumberValue: String,
    onCardNumberChange: (String) -> Unit,
    onProcessCard: () -> Unit,
    onDismiss: () -> Unit,
) {
    val scrollState = rememberScrollState()

    var backProgress by remember(visible) {
        mutableFloatStateOf(0f)
    }

    PredictiveBackHandler(visible) { backEvent ->
        backEvent.collect { event ->
            backProgress = event.progress
        }
        onDismiss.invoke()
        delay(500)
        backProgress = 0f
    }

    AnimatedContent(
        targetState = visible,
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) { targetState ->
        if (targetState) {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("${cardData.cardType}-container-card-key"),
                        animatedVisibilityScope = this@AnimatedContent
                    )
                    .graphicsLayer {
                        val scaleProgress = (1f - backProgress).coerceAtLeast(0.85f)
                        scaleX = scaleProgress
                        scaleY = scaleProgress
                        alpha = scaleProgress
                    }
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Enter Card Number",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                OutlinedTextField(
                    value = cardNumberValue,
                    onValueChange = {
                        if (it.isDigitsOnly()) {
                            onCardNumberChange.invoke(it)
                        }
                    },
                    singleLine = true,
                    placeholder = {
                        Text(text = "e.g. 1234567890")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    isError = cardNumberValue.length < 10 && cardNumberValue.isNotEmpty(),
                    supportingText = {
                        if (cardNumberValue.length < 10 && cardNumberValue.isNotEmpty()) {
                            Text(
                                text = "Card number must be at least 10 characters",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Button(
                    onClick = {
                        onProcessCard.invoke()
                    },
                    enabled = cardNumberValue.length >= 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Process")
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(text = "Cancel", textDecoration = TextDecoration.Underline)
                }
            }
        }
    }
}