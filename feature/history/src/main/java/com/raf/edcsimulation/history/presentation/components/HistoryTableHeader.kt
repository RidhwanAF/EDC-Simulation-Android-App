package com.raf.edcsimulation.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HistoryTableHeader(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .background(MaterialTheme.colorScheme.primary.copy(0.75f))
    ) {
        HistoryTableHeaderItem(
            label = "Id",
            showStartDivider = false,
            showEndDivider = true,
            modifier = Modifier.weight(0.15f)
        )
        HistoryTableHeaderItem(
            label = "Merchant",
            showStartDivider = false,
            modifier = Modifier.weight(0.2f)
        )
        HistoryTableHeaderItem(
            label = "Amount",
            modifier = Modifier.weight(0.25f)
        )
        HistoryTableHeaderItem(
            label = "Card",
            modifier = Modifier.weight(0.2f)
        )
        HistoryTableHeaderItem(
            label = "Status",
            modifier = Modifier.weight(0.2f)
        )
    }
}

@Composable
private fun HistoryTableHeaderItem(
    modifier: Modifier = Modifier,
    label: String = "",
    showStartDivider: Boolean = true,
    showEndDivider: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        VerticalDivider(color = if (showStartDivider) MaterialTheme.colorScheme.primary else Color.Transparent)
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(8.dp)
        )
        VerticalDivider(color = if (showEndDivider) MaterialTheme.colorScheme.primary else Color.Transparent)
    }
}