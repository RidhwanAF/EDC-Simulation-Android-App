package com.raf.edcsimulation.card.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.raf.edcsimulation.card.presentation.viewmodel.CardData

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardMenuItem(
    modifier: Modifier = Modifier,
    cardData: CardData,
    visible: Boolean,
    onMenuClicked: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState("${cardData.cardType}-card-key"),
                    animatedVisibilityScope = this@AnimatedVisibility
                )
                .fillMaxSize()
                .padding(16.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(cardData.cardColor)
                .clickable { onMenuClicked() }
                .padding(32.dp)
        ) {
            Text(
                text = cardData.cardTitle,
                color = cardData.cardTextColor,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}