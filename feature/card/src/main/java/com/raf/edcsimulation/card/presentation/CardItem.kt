package com.raf.edcsimulation.card.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raf.edcsimulation.card.presentation.viewmodel.CardData
import com.raf.edcsimulation.core.domain.model.CardType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardItem(
    modifier: Modifier = Modifier,
    currentType: CardType?,
    listCard: List<CardData>,
    visible: Boolean,
    onCardProcessed: () -> Unit,
    onDismiss: () -> Unit,
) {
    val localConfig = LocalConfiguration.current
    val isLandscape = localConfig.orientation == Configuration.ORIENTATION_LANDSCAPE

    val localHapticFeedback = LocalHapticFeedback.current
    val offset = remember {
        Animatable(Offset.Zero, Offset.VectorConverter)
    }
    val scope = rememberCoroutineScope()

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

    var showHint by remember(visible) {
        mutableStateOf(true)
    }

    LaunchedEffect(visible) {
        if (visible) {
            delay(5000)
            showHint = false
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        listCard.forEach { card ->
            AnimatedVisibility(
                visible = visible && currentType == card.cardType,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ElevatedCard(
                    onClick = {
                        scope.launch {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.GestureEnd)
                            offset.animateTo(
                                Offset(
                                    x = offset.value.x,
                                    y = -2000f
                                )
                            )
                            onCardProcessed.invoke()
                            delay(500)
                            offset.animateTo(Offset.Zero)
                        }
                    },
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = card.cardColor,
                        contentColor = card.cardTextColor
                    ),
                    modifier = modifier
                        .rotate(if (isLandscape) 90f else 0f)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("${card.cardType}-container-card-key"),
                            animatedVisibilityScope = this@AnimatedVisibility
                        )
                        .graphicsLayer {
                            val scaleProgress = (1f - backProgress).coerceAtLeast(0.85f)
                            scaleX = scaleProgress
                            scaleY = scaleProgress
                            alpha = scaleProgress
                            translationY = offset.value.y
                            translationX = offset.value.x
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    scope.launch {
                                        val newOffset = offset.value + dragAmount
                                        offset.snapTo(newOffset)
                                        Log.d("CardItem", "Drag Offset: $newOffset")
                                    }
                                },
                                onDragEnd = {
                                    scope.launch {
                                        if (offset.value.y <= -500f) {
                                            localHapticFeedback.performHapticFeedback(
                                                HapticFeedbackType.GestureEnd
                                            )
                                            offset.animateTo(
                                                Offset(
                                                    x = offset.value.x,
                                                    y = -2000f
                                                )
                                            )
                                            onCardProcessed.invoke()
                                            delay(500)
                                            offset.animateTo(Offset.Zero)
                                        } else if (offset.value.y >= 500f) {
                                            onDismiss.invoke()
                                            offset.animateTo(Offset.Zero)
                                        } else {
                                            offset.animateTo(Offset.Zero)
                                        }
                                    }
                                }
                            )
                        }
                        .padding(16.dp)
                        .aspectRatio(9f / 16f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .fillMaxSize()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CreditCard,
                                    contentDescription = card.cardTitle,
                                    tint = card.cardTextColor,
                                    modifier = Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState("${card.cardType}-icon-card-key"),
                                            animatedVisibilityScope = this@AnimatedVisibility
                                        )
                                        .size(40.dp)
                                )
                                Text(
                                    text = card.cardTitle,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = card.cardTextColor,
                                    fontSize = 32.sp,
                                    modifier = Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState("${card.cardType}-text-card-key"),
                                            animatedVisibilityScope = this@AnimatedVisibility
                                        )
                                )
                            }
                            if (card.cardType == CardType.CHIP) {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 32.dp)
                                        .fillMaxHeight()
                                        .width(64.dp)
                                        .background(card.cardTextColor)
                                )
                            }
                        }

                        if (card.cardType == CardType.CHIP) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(32.dp)
                                    .padding(bottom = 16.dp)
                                    .size(width = 64.dp, height = 72.dp)
                                    .clip(MaterialTheme.shapes.large)
                                    .background(Color.Yellow)
                                    .background(Color.Black.copy(0.15f))
                                    .border(1.dp, Color.Black, MaterialTheme.shapes.large)
                                    .align(Alignment.BottomCenter)
                            ) {
                                HorizontalDivider(
                                    color = Color.Black
                                )
                                VerticalDivider(
                                    color = Color.Black
                                )
                            }
                        }
                        if (card.cardType == CardType.CONTACT_LESS) {
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = "Contact Less",
                                tint = card.cardTextColor,
                                modifier = Modifier
                                    .size(64.dp)
                            )

                            Icon(
                                imageVector = Icons.Default.Nfc,
                                contentDescription = "NFC",
                                tint = card.cardTextColor,
                                modifier = Modifier
                                    .padding(32.dp)
                                    .size(32.dp)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = showHint && visible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .offset(y = 64.dp)
        ) {
            Text(
                text = "Tap or Swipe the Card for Action",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                    .padding(16.dp)
            )
        }
    }
}