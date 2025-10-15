package com.raf.edcsimulation.core.presentation.components

import android.os.Build
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.raf.edcsimulation.core.domain.model.AppSettings
import com.raf.edcsimulation.core.domain.model.DarkTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SettingsDialog(
    modifier: Modifier = Modifier,
    isShown: Boolean,
    onDismissRequest: () -> Unit,
    currentAppSettings: AppSettings,
    onAppSettingsChange: (AppSettings) -> Unit,
) {
    val scrollState = rememberScrollState()

    var backProgress by remember(isShown) {
        mutableFloatStateOf(0f)
    }

    PredictiveBackHandler(enabled = isShown) { backEvent ->
        backEvent.collect { event ->
            backProgress = event.progress
        }
        onDismissRequest.invoke()
        backProgress = 0f
    }

    AnimatedVisibility(
        visible = isShown,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onDismissRequest.invoke()
                        }
                    )
                }
                .background(Color.Black.copy(alpha = 0.75f))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("settings-dialog-key"),
                        animatedVisibilityScope = this@AnimatedVisibility
                    )
                    .graphicsLayer {
                        scaleX = (1f - backProgress).coerceAtLeast(0.85f)
                        scaleY = (1f - backProgress).coerceAtLeast(0.85f)
                    }
                    .widthIn(max = 400.dp)
                    .heightIn(max = 400.dp)
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Title
                Text(
                    text = "Settings".uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 400.dp)
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    // Dark Theme
                    DarkTheme(
                        currentDarkTheme = currentAppSettings.darkTheme,
                        onDarkThemeChange = {
                            onAppSettingsChange.invoke(
                                currentAppSettings.copy(
                                    darkTheme = it
                                )
                            )
                        }
                    )
                    // Dynamic Theme Android 12+
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        onAppSettingsChange.invoke(
                                            currentAppSettings.copy(
                                                dynamicColor = !currentAppSettings.dynamicColor
                                            )
                                        )
                                    }
                                )
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Dynamic Color",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = currentAppSettings.dynamicColor,
                                onCheckedChange = {
                                    onAppSettingsChange.invoke(
                                        currentAppSettings.copy(
                                            dynamicColor = it
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
                // Action Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text(
                            text = "Close",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.DarkTheme(
    modifier: Modifier = Modifier,
    currentDarkTheme: DarkTheme = DarkTheme.FOLLOW_SYSTEM,
    onDarkThemeChange: (DarkTheme) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Dark Theme",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(48.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .padding(4.dp)
        ) {
            DarkTheme.entries.forEach { darkTheme ->
                val isSelected = darkTheme == currentDarkTheme
                val imageVector = when (darkTheme) {
                    DarkTheme.FOLLOW_SYSTEM -> if (isSelected) Icons.Filled.Smartphone else Icons.Outlined.Smartphone
                    DarkTheme.LIGHT -> if (isSelected) Icons.Filled.LightMode else Icons.Outlined.LightMode
                    DarkTheme.DARK -> if (isSelected) Icons.Filled.DarkMode else Icons.Outlined.DarkMode
                }
                val title = when (darkTheme) {
                    DarkTheme.FOLLOW_SYSTEM -> "System"
                    DarkTheme.LIGHT -> "Light"
                    DarkTheme.DARK -> "Dark"
                }
                val iconColorAnimation by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    AnimatedContent(
                        targetState = isSelected,
                        modifier = Modifier
                            .zIndex(2f)
                    ) { targetState ->
                        if (targetState) {
                            Box(
                                modifier = Modifier
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState("theme-key"),
                                        animatedVisibilityScope = this@AnimatedContent,
                                        zIndexInOverlay = 1f
                                    )
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            onDarkThemeChange.invoke(darkTheme)
                        },
                        modifier = Modifier
                            .zIndex(3f)
                    ) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = title,
                            tint = iconColorAnimation
                        )
                    }
                }
            }
        }
    }
}