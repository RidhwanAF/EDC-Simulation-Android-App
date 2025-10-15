package com.raf.edcsimulation.card.presentation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raf.edcsimulation.card.presentation.viewmodel.CardData
import com.raf.edcsimulation.core.domain.model.CardType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    card: CardData?,
    visible: Boolean,
    onCardProcessed: () -> Unit,
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val offset = remember {
        Animatable(Offset.Zero, Offset.VectorConverter)
    }
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = visible && card != null,
        enter = scaleIn(),
        exit = scaleOut()
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
                containerColor = card?.cardColor ?: MaterialTheme.colorScheme.primary,
                contentColor = card?.cardTextColor ?: MaterialTheme.colorScheme.onPrimary
            ),
            modifier = modifier
                .graphicsLayer {
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
                            contentDescription = card?.cardTitle,
                            tint = card?.cardTextColor ?: MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = card?.cardTitle ?: "Card",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = card?.cardTextColor ?: MaterialTheme.colorScheme.onPrimary,
                            fontSize = 32.sp
                        )
                    }
                    if (card?.cardType == CardType.CHIP) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxHeight()
                                .width(64.dp)
                                .background(card.cardTextColor)
                        )
                    }
                }

                if (card?.cardType == CardType.CHIP) {
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
                if (card?.cardType == CardType.CONTACT_LESS) {
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