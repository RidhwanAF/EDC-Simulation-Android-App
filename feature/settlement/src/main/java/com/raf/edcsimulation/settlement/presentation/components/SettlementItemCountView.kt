package com.raf.edcsimulation.settlement.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SettlementItemCountView(
    modifier: Modifier = Modifier,
    title: String,
    count: Int,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val localConfig = LocalConfiguration.current
    val isLandscape = localConfig.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(containerColor, MaterialTheme.shapes.extraLarge)
                .border(2.dp, textColor, MaterialTheme.shapes.extraLarge)
                .padding(16.dp)
        ) {
            Text(
                text = "$title: ",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = textColor
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(containerColor, MaterialTheme.shapes.extraLarge)
                .border(2.dp, textColor, MaterialTheme.shapes.extraLarge)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = textColor
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
    }
}