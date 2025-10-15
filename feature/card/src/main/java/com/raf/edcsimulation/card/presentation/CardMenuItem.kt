package com.raf.edcsimulation.card.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Icon
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
    AnimatedContent(
        targetState = visible,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        contentAlignment = Alignment.Center,
    ) { targetState ->
        if (targetState) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("${cardData.cardType}-container-card-key"),
                        animatedVisibilityScope = this@AnimatedContent
                    )
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(cardData.cardColor)
                    .clickable { onMenuClicked() }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CreditCard,
                    contentDescription = cardData.cardTitle,
                    tint = cardData.cardTextColor,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("${cardData.cardType}-icon-card-key"),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                        .size(40.dp)
                )
                Text(
                    text = cardData.cardTitle,
                    color = cardData.cardTextColor,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("${cardData.cardType}-text-card-key"),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                )
            }
        } else {
            Spacer(
                modifier = modifier
                    .fillMaxSize()
                    .padding(48.dp)
            )
        }
    }
}